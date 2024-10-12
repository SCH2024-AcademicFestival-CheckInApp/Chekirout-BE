package com.sch.chekirout.user.application;

import com.sch.chekirout.device.Serivce.DeviceService;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.UserRole;
import com.sch.chekirout.user.dto.request.UserRequest;
import com.sch.chekirout.user.dto.response.UserResponseDto;
import com.sch.chekirout.user.exception.PasswordMismatchException;
import com.sch.chekirout.user.exception.StudentIdAlreayExists;
import com.sch.chekirout.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    private final DeviceService deviceService;

    @Transactional
    public User registerUser(UserRequest userRequest) {
        validateUsernameAvailability(userRequest.getUsername());
        User savedUser = userRepository.save(userRequest.toEntity(passwordEncoder.encode(userRequest.getPassword())));
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
}