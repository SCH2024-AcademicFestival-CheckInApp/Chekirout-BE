package com.sch.chekirout.auth.application;



import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;
import com.sch.chekirout.device.Repository.UserDeviceRepository;
import com.sch.chekirout.device.Serivce.DeviceService;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.device.exception.DeviceNotFoundException;
import com.sch.chekirout.device.exception.DeviceNotMatchException;
import com.sch.chekirout.device.util.UserAgentUtil;
import com.sch.chekirout.common.exception.ErrorCode;
import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.UserStatus;
import com.sch.chekirout.user.exception.EmailNotVerifiedException;
import com.sch.chekirout.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DeviceService deviceService;  // DeviceService 추가

    public CustomUserDetailsService(UserRepository userRepository, DeviceService deviceService) {
        this.userRepository = userRepository;
        this.deviceService = deviceService;
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 학번이 8자리 숫자인지 확인
        if (!username.matches("^[0-9]{8}$")) {
            throw new UsernameNotFoundException("Invalid username format. Username should be an 8-digit student number.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));


        // 디바이스 검증 로직 호출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        deviceService.validateDevice(user, request);



        // 사용자가 PENDING 상태이면 로그인 불가
        if (user.getStatus() == UserStatus.PENDING) {
            throw new EmailNotVerifiedException();
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
