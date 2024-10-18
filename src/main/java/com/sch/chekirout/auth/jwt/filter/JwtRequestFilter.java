package com.sch.chekirout.auth.jwt.filter;

import com.sch.chekirout.auth.jwt.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        // 특정 경로에 대해서는 JWT 필터 적용하지 않음
        if (request.getRequestURI().equals("/api/v1/signup") ||
                request.getRequestURI().equals("/api/v1/login") ||
                request.getRequestURI().equals("/api/v1/auth/verify-email") ||
                request.getRequestURI().equals("/api/v1/auth/checkEmail") ||
                request.getRequestURI().equals("/api/v1/auth/validate-email")
        ){
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = null;

        // JWT 토큰이 "Bearer "로 시작하는지 확인
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.info("Authorization Header: " + request.getHeader("Authorization"));
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // 사용자 이름이 존재하고 SecurityContext에 인증 정보가 없는 경우 설정
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // JWT 토큰의 유효성을 검증하고, SecurityContext에 인증 정보 설정
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                // JWT의 유효성 검증이 성공하면, SecurityContext에 설정
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청을 전달
        chain.doFilter(request, response);

    }
}
