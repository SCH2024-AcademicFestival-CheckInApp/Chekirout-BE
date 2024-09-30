package com.sch.chekirout.Security.controller;



import com.sch.chekirout.Security.Service.CustomUserDetailsService;
import com.sch.chekirout.Security.Service.UserService;
import com.sch.chekirout.Security.model.User;
import com.sch.chekirout.Security.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // 회원가입 로직 처리
        if (userService.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("Username already exists!");
        }
    }
}
