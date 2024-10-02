package com.sch.chekirout.Security.controller;



import com.sch.chekirout.Security.DTO.UserRequestDto;
import com.sch.chekirout.Security.Service.CustomUserDetailsService;
import com.sch.chekirout.Security.Service.UserService;
import com.sch.chekirout.Security.model.JwtRequest;
import com.sch.chekirout.Security.model.JwtResponse;
import com.sch.chekirout.Security.model.User;
import com.sch.chekirout.Security.Service.CustomUserDetailsService;
import com.sch.chekirout.Security.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        // 회원가입 로직 처리
        // 유효성 검사 실패 시 에러 메시지 반환
        if (bindingResult.hasErrors()) {
            // `BindingResult`의 모든 에러 메시지를 문자열로 변환하여 반환
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())  // 각 에러 메시지 추출
                    .collect(Collectors.joining(", "));  // 메시지들을 하나의 문자열로 결합
            return ResponseEntity.badRequest().body(errors);  // 문자열 형식으로 반환
        }

        // 유효성 검증 통과 후 회원가입 처리
        boolean isRegistered = userService.registerUser(userRequestDto);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("User ID already exists or invalid data!");
        }
    }

    // 2. 새로운 로그인 인증 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
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
