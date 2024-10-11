package com.sch.chekirout.user.presentation;


import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.ChangePasswordRequestDto;
import com.sch.chekirout.user.dto.request.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/validate-username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
        // 학번 형식 검증 (8자리 숫자)
        if (!username.matches("^[0-9]{8}$")) {
            return ResponseEntity.badRequest().body("학번은 8자리 숫자여야 합니다.");
        }

        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    // 3. 현재 로그인된 사용자 정보 조회
    @Operation(
            summary = "사용자 프로필 조회",
            description = "현재 로그인된 사용자의 프로필 정보를 조회하는 API"
    )
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userService.findUserByUsername(currentUsername);
        UserResponseDto responseDto = new UserResponseDto(user);

        return ResponseEntity.ok(responseDto);
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
