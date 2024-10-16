package com.sch.chekirout.user.application;

import com.sch.chekirout.device.Serivce.DeviceService;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.common.exception.ErrorCode;
import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.email.repository.EmailVerificationTokenRepository;
import com.sch.chekirout.email.service.EmailService;
import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.UserRole;
import com.sch.chekirout.user.dto.request.UserRequest;
import com.sch.chekirout.user.dto.response.UserResponseDto;
import com.sch.chekirout.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    private final DeviceService deviceService;


    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;



    @Transactional
    public User registerUser(UserRequest userRequest) {
        validateUsernameAvailability(userRequest.getUsername());

        validateEmailAvailability(userRequest.getEmail());  // 이메일 중복 검사 추가


        // 사용자 저장
        User user = userRequest.toEntity(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);

//
//        // 이메일 인증 토큰 생성 및 이메일 발송
//        String token = generateEmailVerificationToken(savedUser);
//        emailService.sendVerificationEmail(user.getEmail(), token);
        return savedUser;
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public void validateUsernameAvailability(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new StudentIdAlreayExists();
        }
    }

    @Transactional(readOnly = true)
    public void validateEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExists();  // 이메일 중복 오류
        }
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsersOffsetPaging(Department department, Pageable pageable) {

        Page<User> usersPage = (department != null)
                ? userRepository.findByDepartment(department, pageable)
                : userRepository.findAll(pageable);

        return usersPage.map(user -> {
            // 사용자에 연결된 UserDevice 가져오기
            Optional<UserDevice> userDevice = deviceService.findDeviceByUserId(user.getId());

            // 기기 이름을 가져오거나 null 처리
            String deviceName = userDevice.map(UserDevice::getDeviceName).orElse("Unknown Device");

            // UserResponseDto 생성 시 deviceName 추가
            return UserResponseDto.from(user, deviceName);
        });
    }

    @Transactional
    public void updateUserRole(String username, UserRole newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        user.updateRole(newRole);
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // 현재 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordMismatchException();
        }

        // 새로운 비밀번호로 변경 및 암호화
        user.updatePassword(newPassword, passwordEncoder);
    }


//    // 이메일 재발송 기능
//    @Transactional
//    public void resendVerificationToken(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException(email));
//
//        // 기존 토큰을 가져오고 만료되었는지 확인
//        EmailVerificationToken existingToken = tokenRepository.findByUser(user)
//                .orElseThrow(() -> new TokenNotFoundException());
//
//        if (!existingToken.isExpired()) {
//            throw new TokenNotExpiredException();  // 토큰이 아직 만료되지 않은 경우
//        }
//
//        // 새 토큰 생성 및 저장
//        String newToken = UUID.randomUUID().toString();
//        EmailVerificationToken newVerificationToken = new EmailVerificationToken(newToken, user);
//        tokenRepository.save(newVerificationToken);
//
//        // 새 토큰 이메일 발송
//        emailService.sendVerificationEmail(user.getEmail(), newToken);
//    }


}