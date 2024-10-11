package com.sch.chekirout.user.presentation;

import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.RoleUpdateRequestDto;
import com.sch.chekirout.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {

    private final UserService userService;

    @Operation(
            summary = "사용자 목록 조회",
            description = "사용자 목록을 조회하는 API, 학과별 필터링 가능 ex) /api/v1/admin/users?department=CSE&page=0&size=20, default size=20"
    )
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) Department department,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsersOffsetPaging(department, pageable));
    }

    @Operation(
            summary = "학번으로 특정 사용자 조회",
            description = "학번으로 특정 사용자 정보를 조회하는 API"
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {

        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }


    @Operation(
            summary = "사용자 권한 변경",
            description = "특정 사용자의 권한을 변경하는 API"
    )
    @PreAuthorize("hasRole('MASTER')")  // MASTER 권한을 가진 사용자만 접근 가능
    @PutMapping("/{username}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable String username, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) {

        userService.updateUserRole(username, roleUpdateRequestDto.getRole());

        return ResponseEntity.ok("사용자 권한이 변경되었습니다.");
    }
}
