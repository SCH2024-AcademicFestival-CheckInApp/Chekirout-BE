package com.sch.chekirout.notification;

import com.sch.chekirout.notification.NotificationService;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final ProgramRepository programRepository;

    // 예약된 알림 작업을 저장하는 맵 (프로그램 ID와 예약 시간 매핑)
    private final ConcurrentMap<String, LocalDateTime> scheduledNotifications = new ConcurrentHashMap<>();

    public NotificationScheduler(NotificationService notificationService, ProgramRepository programRepository) {
        this.notificationService = notificationService;
        this.programRepository = programRepository;
    }

    // 알림 예약 추가 (10분 전 알림 시간으로 설정)
    public void scheduleNotification(Program program) {

        // 프로그램 시작 시간 10분 전으로 알림 예약 설정
        LocalDateTime notificationTime = program.getStartTimestamp().minusMinutes(10);
        scheduledNotifications.put(program.getId(), notificationTime);
    }

    // 매분 실행되어 예약된 알림 작업을 확인하고 실행
    @Scheduled(fixedRate = 10000) // 10초마다 실행
    public void checkAndSendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("알람체크 " + now);

        scheduledNotifications.forEach((programId, scheduledTime) -> {
            if (now.isAfter(scheduledTime) || now.isEqual(scheduledTime)) {
                Program program = programRepository.findByIdAndDeletedAtIsNull(String.valueOf(programId)).orElse(null);

                if (program != null && !program.isNotification()) {
                    notificationService.sendProgramNotifications(program);
                    program.setNotificationSent();
                    programRepository.save(program);
                    System.out.println(now + "알람을 보냈습니다");

                    // 예약된 알림 작업에서 제거
                    scheduledNotifications.remove(programId);
                }
            }
        });
    }
}
