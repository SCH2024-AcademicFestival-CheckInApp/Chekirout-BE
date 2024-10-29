package com.sch.chekirout.notification;

import com.sch.chekirout.notification.FCMtoken.application.FCMService;
import com.sch.chekirout.notification.FCMtoken.domain.FCMToken;
import com.sch.chekirout.notification.FCMtoken.domain.Repository.FCMTokenRepository;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FCMService fcmService;
    private final FCMTokenRepository fcmTokenRepository;
    private final ProgramRepository programRepository;

    @Async
    public void sendProgramNotifications(Program program) {
        // 알림을 이미 보냈는지 확인
        if (program.isNotification()) {
            // 알림이 이미 전송된 경우
            return;
        }

        // fcmTokenRepository에서 FCM 토큰 목록 조회
        List<FCMToken> tokens = fcmTokenRepository.findAll();

        for (FCMToken token : tokens) {
            String fcmToken = token.getToken();
            fcmService.sendNotification(
                    fcmToken,
                    program.getName() + " 시작 알림",
                    "10분 후에 " + program.getName() + "이(가) 시작됩니다. 준비해 주세요!"
            );
        }

        // 알림 전송 후, notificationYn 업데이트
        program.setNotificationSent();  // 알림 전송 상태로 변경
        programRepository.save(program);
    }
}
