package com.sch.chekirout.device.domain;  // 패키지 선언 추가

import com.sch.chekirout.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceName;
    private String operatingSystem;
    private String browser;
    private String ipAddress;
    private String userAgent;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 명시적 메서드를 통한 필드 설정
    public static UserDevice createDevice(User user, String deviceName, String operatingSystem, String browser, String ipAddress, String userAgent) {
        UserDevice userDevice = new UserDevice();
        userDevice.user = user;  // User와의 연관 관계 설정
        userDevice.deviceName = deviceName;
        userDevice.operatingSystem = operatingSystem;
        userDevice.browser = browser;
        userDevice.ipAddress = ipAddress;
        userDevice.userAgent = userAgent;
        return userDevice;
    }

    // 필드 업데이트 메서드 (예: 기기 정보가 변경되었을 경우)
    public void updateDeviceInfo(String deviceName, String operatingSystem, String browser, String ipAddress, String userAgent) {
        this.deviceName = deviceName;
        this.operatingSystem = operatingSystem;
        this.browser = browser;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
}