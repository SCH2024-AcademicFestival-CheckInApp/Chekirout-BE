package com.sch.chekirout.user.presentation;


import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.ChangePasswordRequestDto;
import com.sch.chekirout.user.dto.request.RoleUpdateRequestDto;
import com.sch.chekirout.user.dto.request.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 전체 사용자 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 2. 특정 사용자 조회
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // 3. 현재 로그인된 사용자 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);
        return ResponseEntity.ok(user);
    }


    // 사용자 권한 수정 엔드포인트
    @PreAuthorize("hasRole('MASTER')")  // MASTER 권한을 가진 사용자만 접근 가능
    @PutMapping("/{username}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable String username, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) {
        boolean isUpdated = userService.updateUserRole(username, roleUpdateRequestDto.getRole());

        if (isUpdated) {
            return ResponseEntity.ok("User role updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update user role.");
        }
    }

    // 비밀번호 변경 엔드포인트
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        // 현재 로그인된 사용자의 이름(username)을 SecurityContext에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 비밀번호 변경 서비스 호출
        boolean isPasswordChanged = userService.changePassword(
                currentUsername,
                changePasswordRequestDto.getCurrentPassword(),
                changePasswordRequestDto.getNewPassword()
        );

        if (isPasswordChanged) {
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid current password.");
        }
    }
}
