package com.sch.chekirout.user.application;

import com.sch.chekirout.user.domain.UserRole;
import com.sch.chekirout.user.dto.request.UserRequest;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.UserResponseDto;
import com.sch.chekirout.user.exception.StudentIdAlreayExists;
import com.sch.chekirout.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean registerUser(UserRequest userRequest) {
        // DTO에서 엔티티로 변환
        User user = new User(
                userRequest.getUsername(),
                userRequest.getDepartment(),
                userRequest.getName(),
                passwordEncoder.encode(userRequest.getPassword()),  // 암호화된 비밀번호 설정
                userRequest.getRole()  // Role 설정 (기본값: STUDENT)
        );

        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    public String existsByUsername(String username) {
        boolean isExisting =  userRepository.existsByUsername(username);

        if (isExisting) {
            throw new StudentIdAlreayExists();
        }

        return "사용 가능한 학번입니다.";
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUserRole(String username, UserRole newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        user.updateRole(newRole);
    }

    @Transactional
    public boolean changePassword(String username, String currentPassword, String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // 현재 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;  // 현재 비밀번호가 일치하지 않음
        }

        // 새로운 비밀번호로 변경 및 암호화
        user.updatePassword(newPassword, passwordEncoder);
        return true;
    }
}
