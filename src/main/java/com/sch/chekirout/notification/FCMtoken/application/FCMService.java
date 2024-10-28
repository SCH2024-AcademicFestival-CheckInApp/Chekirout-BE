package com.sch.chekirout.notification.FCMtoken.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sch.chekirout.notification.FCMtoken.domain.FCMToken;
import com.sch.chekirout.notification.FCMtoken.domain.Repository.FCMTokenRepository;
import com.sch.chekirout.notification.FCMtoken.dto.FCMTokenResponse;
import com.sch.chekirout.user.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final FCMTokenRepository fcmTokenRepository;

    // FCM 토큰 저장 메서드
    // 매개변수의 토큰은 프론트와 얘기해봐야함
    public void saveToken(UserRequest userRequest) {
        // FCMToken 엔티티 생성
        FCMToken fcmToken = new FCMToken();

        // UserDetails에서 사용자 정보를 가져와 FCMToken에 설정
        fcmToken.setEmail(userRequest.getUsername()); // username이 이메일로 사용되는 경우
        fcmToken.setName(userRequest.getUsername());  // UserDetails에서 이름을 얻을 수 없으므로 username 사용
        fcmToken.setToken(userRequest.getToken()); // 전달받은 FCM 토큰

        // fcmToken 엔티티를 저장
        fcmTokenRepository.save(fcmToken);
    }

    // 이메일로 토큰 조회
    public FCMTokenResponse getToken(String email) {
        FCMToken fcmToken = fcmTokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
        return new FCMTokenResponse(fcmToken.getEmail(), fcmToken.getName(), fcmToken.getToken());
    }

    // 이메일로 토큰 삭제
    public void deleteToken(String email) {
        fcmTokenRepository.deleteByEmail(email);
    }

    public void sendNotification(String fcmToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken) // FCM 토큰
                .setNotification(notification)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("푸시 알림 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
