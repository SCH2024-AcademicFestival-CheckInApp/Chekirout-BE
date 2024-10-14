package com.sch.chekirout.user.presentation;


import com.sch.chekirout.device.Serivce.DeviceService;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.ChangePasswordRequestDto;
import com.sch.chekirout.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;  // DeviceService 의존성 주입

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "학번 중복 검사",
            description = "학번 중복 검사 API"
    )
    @GetMapping("/validate-username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
        // 학번 형식 검증 (8자리 숫자)
        if (!username.matches("^[0-9]{8}$")) {
            return ResponseEntity.badRequest().body("학번은 8자리 숫자여야 합니다.");
        }
        userService.validateUsernameAvailability(username);

        return ResponseEntity.ok().body("사용 가능한 학번입니다.");
    }

    @Operation(
            summary = "사용자 프로필 조회",
            description = "현재 로그인된 사용자의 프로필 정보를 조회하는 API"
    )
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userService.findUserByUsername(currentUsername);

        // 3. 사용자에 연결된 기기 정보 조회
        Optional<UserDevice> userDevice = deviceService.findDeviceByUserId(user.getId());

        // 4. 기기 이름 추출 (없을 경우 'Unknown Device' 처리)
        String deviceName = userDevice.map(UserDevice::getDeviceName).orElse("Unknown Device");

        // 5. UserResponseDto 생성 시 deviceName 포함하여 반환
        return ResponseEntity.ok(UserResponseDto.from(user, deviceName));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인된 사용자의 비밀번호를 변경하는 API"
    )
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        // 현재 로그인된 사용자의 이름(username)을 SecurityContext에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 비밀번호 변경 서비스 호출
        userService.changePassword(
                currentUsername,
                changePasswordRequestDto.getCurrentPassword(),
                changePasswordRequestDto.getNewPassword()
        );

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
