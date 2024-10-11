package com.sch.chekirout.user.application;

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

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserRequest userRequest) {
        validateUsernameAvailability(userRequest.getUsername());
        userRepository.save(userRequest.toEntity(passwordEncoder.encode(userRequest.getPassword())));
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

        return usersPage.map(UserResponseDto::from);
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