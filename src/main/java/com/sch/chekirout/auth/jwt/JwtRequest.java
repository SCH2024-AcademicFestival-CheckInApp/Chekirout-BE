package com.sch.chekirout.auth.jwt;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotEmpty(message = "학번(username)은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]{8}$", message = "학번(username)은 8자리 숫자여야 합니다.")  // 학번 형식 검증
    private String username;
    private String password;

    // 기본 생성자 필요
    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
