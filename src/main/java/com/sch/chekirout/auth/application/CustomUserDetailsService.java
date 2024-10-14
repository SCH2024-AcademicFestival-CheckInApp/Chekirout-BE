package com.sch.chekirout.auth.application;



import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;
import com.sch.chekirout.device.Repository.UserDeviceRepository;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.device.exception.DeviceNotFoundException;
import com.sch.chekirout.device.exception.DeviceNotMatchException;
import com.sch.chekirout.device.util.UserAgentUtil;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.Repository.UserRepository;
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
//import com.sch.chekirout.Security.model.User;
//import com.sch.chekirout.Security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserDeviceRepository userDeviceRepository) {
        this.userRepository = userRepository;
        this.userDeviceRepository = userDeviceRepository;
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


        // 현재 요청에서 HttpServletRequest 객체 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String userAgent = request.getHeader("User-Agent");  // 로그인 시 요청에서 userAgent 값 추출
        System.out.println("로그인 시 받아온 userAgent: " + userAgent);  // 로그로 출력하여 확인


        // userAgent에서 디바이스 정보 추출
        String currentDeviceInfo = UserAgentUtil.extractDeviceInfo(request.getHeader("User-Agent"));
        System.out.println("추출된 디바이스 정보: " + currentDeviceInfo);

        // 기존에 저장된 디바이스 정보 조회
        UserDevice existingDevice = userDeviceRepository.findByUser(user)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found for user: " + username));


        // 현재 디바이스 정보와 기존 디바이스 정보 비교
        if (!existingDevice.getDeviceInfo().equals(currentDeviceInfo)) {
            throw new DeviceNotMatchException();
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
