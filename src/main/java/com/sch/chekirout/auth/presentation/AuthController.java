package com.sch.chekirout.auth.presentation;



import com.sch.chekirout.device.Serivce.DeviceService;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.device.util.DeviceInfoUtil;
import com.sch.chekirout.device.util.UserAgentUtil;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.dto.request.UserRequest;
import com.sch.chekirout.auth.application.CustomUserDetailsService;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.auth.jwt.JwtRequest;
import com.sch.chekirout.auth.jwt.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // 1. 이메일 인증 요청
    @PostMapping("/signup/email")
    public ResponseEntity<String> sendEmailVerification(@RequestBody @Valid EmailRequest emailRequest) {
        // 이메일 중복 체크
        userService.validateEmailAvailability(emailRequest.getEmail());

        // 이메일 인증 토큰 생성 및 이메일 발송
        String token = userService.generateEmailVerificationToken(emailRequest.getEmail());

        return ResponseEntity.ok("인증을 위한 이메일이 발송되었습니다.");
    }

    // 2. 이메일 인증 후 최종 회원가입 요청
    @PostMapping("/signup/final")
    public ResponseEntity<String> completeSignup(@RequestBody @Valid UserRequest userRequest, @RequestParam("token") String token, HttpServletRequest request) {
        // 이메일 인증 확인 및 회원가입 처리
        boolean isVerified = userService.verifyEmail(token);
        if (!isVerified) {
            return ResponseEntity.badRequest().body("이메일 인증이 완료되지 않았습니다.");
        }

        // 유저 정보 저장
        User newUser = userService.registerUser(userRequest);

        // 디바이스 정보 추출 및 저장
        String userAgent = request.getHeader("User-Agent");
        String deviceInfo = UserAgentUtil.extractDeviceInfo(userAgent);
        if (deviceInfo == null && userAgent.contains("PostmanRuntime")) {
            deviceInfo = "Postman Device";
        }

        String deviceName = DeviceInfoUtil.extractDeviceName(request);
        String operatingSystem = DeviceInfoUtil.extractOperatingSystem(request);
        String browser = DeviceInfoUtil.extractBrowser(request);
        String ipAddress = DeviceInfoUtil.extractIpAddress(request);

        // 디바이스 정보와 함께 UserDevice 객체 생성 및 저장
        UserDevice userDevice = UserDevice.createDevice(newUser, deviceName, operatingSystem, browser, ipAddress, userAgent, deviceInfo);
        deviceService.saveOrUpdateDevice(userDevice);

        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }


    // 2. 새로운 로그인 인증 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDetails userDetails;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // 비밀번호 검증을 위한 디버그 메시지
            userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            System.out.println("입력된 비밀번호: " + authenticationRequest.getPassword());
            System.out.println("저장된 암호화 비밀번호: " + userDetails.getPassword());
            System.out.println("비밀번호 일치 여부: " + passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword()));

            return ResponseEntity.badRequest().body("Incorrect username or password");
        }

        userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());

        // 디바이스 검증
        deviceService.validateDevice(user, request);


        final String accessToken = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);




        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtTokenUtil.validateToken(refreshToken, customUserDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(refreshToken)))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }

        // Refresh 토큰이 유효하면 새로운 Access 토큰 생성
        String newAccessToken = jwtTokenUtil.generateToken(customUserDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(refreshToken)));
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }

}
