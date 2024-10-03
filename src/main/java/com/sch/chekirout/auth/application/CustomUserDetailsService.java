package com.sch.chekirout.auth.application;



import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
//import com.sch.chekirout.Security.model.User;
//import com.sch.chekirout.Security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 학번이 8자리 숫자인지 확인
        if (!username.matches("^[0-9]{8}$")) {
            throw new UsernameNotFoundException("Invalid username format. Username should be an 8-digit student number.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 사용자의 권한 설정
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));  // ROLE_ADMIN 또는 ROLE_STUDENT


        // UserDetails 객체 반환 - 여기서 반환된 비밀번호가 올바른지 확인
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
    }
}
