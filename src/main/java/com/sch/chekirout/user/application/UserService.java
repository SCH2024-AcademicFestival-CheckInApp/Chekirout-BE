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
import com.sch.chekirout.user.domain.UserStatus;
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
        // 유효성 검사
        validateUsernameAvailability(userRequest.getUsername());
        validateEmailAvailability(userRequest.getEmail());

        // User 객체 생성 시 department 값을 포함해서 생성
        User user = User.builder()
                .username(userRequest.getUsername())
                .department(userRequest.getDepartment())  // department 값 추가
                .name(userRequest.getName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .role(UserRole.STUDENT)  // 기본값으로 STUDENT 설정
                .status(UserStatus.PENDING)  // 기본 상태 PENDING 설정
                .build();

        return userRepository.save(user);
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

    @Transactional
    public boolean verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException());

        // 토큰 만료 여부 확인
        if (verificationToken.isExpired()) {
            throw new TokenNotExpiredException();
        }

        // 인증된 이메일을 user_info 테이블에 저장
        User user = verificationToken.getUser();  // Token에서 User를 가져옴
        user.activateAccount();  // 계정 활성화 (PENDING -> ACTIVE)
        userRepository.save(user);

        // 토큰 삭제
        tokenRepository.delete(verificationToken);

        return true;
    }



    public String generateEmailVerificationToken(String email) {
        validateEmailAvailability(email);

        // 새로운 User 객체 생성 (아직 데이터베이스에 저장되지 않음)
        User user = new User(email);


        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user);
        tokenRepository.save(verificationToken);

        // 이메일 발송
        emailService.sendVerificationEmail(email, token);
        return token;
    }


    // 이메일 재발송 기능
    @Transactional
    public void resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // 기존 토큰을 가져오고 만료되었는지 확인
        EmailVerificationToken existingToken = tokenRepository.findByUser(user)
                .orElseThrow(() -> new TokenNotFoundException());

        if (!existingToken.isExpired()) {
            throw new TokenNotExpiredException();  // 토큰이 아직 만료되지 않은 경우
        }

        // 새 토큰 생성 및 저장
        String newToken = UUID.randomUUID().toString();
        EmailVerificationToken newVerificationToken = new EmailVerificationToken(newToken, user);
        tokenRepository.save(newVerificationToken);

        // 새 토큰 이메일 발송
        emailService.sendVerificationEmail(user.getEmail(), newToken);
    }


}