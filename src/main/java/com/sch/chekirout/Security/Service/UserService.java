package com.sch.chekirout.Security.Service;

import com.sch.chekirout.Security.DTO.UserRequestDto;
import com.sch.chekirout.Security.Repository.UserRepository;
import com.sch.chekirout.Security.model.User;
import com.sch.chekirout.Security.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(UserRequestDto userRequestDto) {
        // DTO에서 엔티티로 변환
        User user = new User();
        user.setUsername(userRequestDto.getUsername());  // username 설정
        user.setDepartment(userRequestDto.getDepartment());
        user.setName(userRequestDto.getName());

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(userRequestDto.getRole());  // Role 설정

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

}
