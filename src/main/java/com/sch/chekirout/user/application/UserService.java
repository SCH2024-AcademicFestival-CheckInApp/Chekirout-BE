package com.sch.chekirout.user.application;

import com.sch.chekirout.user.domain.UserRole;
import com.sch.chekirout.user.dto.request.UserRequest;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public boolean updateUserRole(String username, UserRole newRole) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // 사용자 권한 업데이트
            user.updateRole(newRole);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // 비밀번호 변경 메서드
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        // 현재 사용자를 DB에서 조회
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return false;  // 사용자 없음
        }

        // 현재 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;  // 현재 비밀번호가 일치하지 않음
        }

        // 새로운 비밀번호로 변경 및 암호화
        user.updatePassword(newPassword, passwordEncoder);
        userRepository.save(user);
        return true;
    }
}
