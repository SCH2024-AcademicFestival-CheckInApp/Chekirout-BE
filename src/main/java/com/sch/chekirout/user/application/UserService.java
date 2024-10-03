package com.sch.chekirout.user.application;

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
        User user = new User();
        user.setUsername(userRequest.getUsername());  // username 설정
        user.setDepartment(userRequest.getDepartment());
        user.setName(userRequest.getName());

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());  // Role 설정

        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        // User 엔티티를 UserResponseDto로 변환하여 반환
        return users.stream()
                .map(user -> new UserResponseDto(
                        user.getUsername(),
                        user.getDepartment(),
                        user.getName(),
                        user.getIsEligibleForPrize(),
                        user.getIsWinner(),
                        user.getIsNotificationEnabled()
                ))
                .collect(Collectors.toList());
    }
}
