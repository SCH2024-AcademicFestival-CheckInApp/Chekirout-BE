package com.sch.chekirout.user.presentation;

import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.RoleUpdateRequestDto;
import com.sch.chekirout.user.dto.request.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {

    private final UserService userService;

    // 1. 전체 사용자 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        // 전체 사용자 정보를 DTO 리스트로 변환하여 반환
        List<UserResponseDto> users = userService.getAllUsers()
                .stream()
                .map(UserResponseDto::new)  // User -> UserResponseDto 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "특정 사용자 조회",
            description = "특정 사용자 정보를 조회하는 API"
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 특정 사용자 정보를 UserResponseDto로 변환하여 반환
        UserResponseDto responseDto = new UserResponseDto(user);

        return ResponseEntity.ok(responseDto);
    }


    @Operation(
            summary = "사용자 권한 변경",
            description = "특정 사용자의 권한을 변경하는 API"
    )
    @PreAuthorize("hasRole('MASTER')")  // MASTER 권한을 가진 사용자만 접근 가능
    @PutMapping("/{username}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable String username, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) {
        boolean isUpdated = userService.updateUserRole(username, roleUpdateRequestDto.getRole());

        if (isUpdated) {
            return ResponseEntity.ok("사용자 권한이 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
        }
    }
}
