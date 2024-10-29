package com.sch.chekirout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.sch.chekirout.notification.FCMtoken.application.FCMService;
import com.sch.chekirout.notification.FCMtoken.domain.FCMToken;
import com.sch.chekirout.notification.FCMtoken.domain.Repository.FCMTokenRepository;
import com.sch.chekirout.notification.NotificationService;
import com.sch.chekirout.program.domain.Category;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.CategoryRepository;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import com.sch.chekirout.program.domain.util.ProgramIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationServiceTest {


    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Mock
    private FCMTokenRepository fcmTokenRepository;

    @Mock
    private FCMService fcmService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private NotificationService notificationService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendProgramNotificationsToMultipleUsers() throws FirebaseMessagingException {
        // 가짜 FCMToken 리스트 생성
        List<FCMToken> tokens = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            FCMToken token = new FCMToken("user" + i + "@example.com", "User " + i, "fake_token_" + i);
            tokens.add(token);
        }

        // Mock 설정: fcmTokenRepository.findAll()이 호출될 때 가짜 토큰 리스트 반환
        Mockito.when(fcmTokenRepository.findAll()).thenReturn(tokens);

        // Mock 설정: fcmService.sendNotification() 메서드를 호출 시 아무 일도 하지 않음
        Mockito.doNothing().when(fcmService).sendNotification(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        // Category 및 Program 객체 생성
        Category category = Category.builder()
                .name("Test Cate")
                .description("test")
                .build();

        Category savedCategory = categoryRepository.save(category);

        Program program = Program.builder()
                .id(ProgramIdGenerator.generateProgramId(category.getId(), LocalDateTime.now()))
                .name("Test Program")
                .description("This is a test program")
                .category(category)
                .startTimestamp(LocalDateTime.now().plusMinutes(10))
                .endTimestamp(LocalDateTime.now().plusHours(1))
                .isNotification(false)
                .build();

        programRepository.save(program);


        // 알림 전송 테스트
        notificationService.sendProgramNotifications(program);

        // sendNotification 메서드가 1000번 호출되었는지 검증
        Mockito.verify(fcmService, Mockito.times(1000)).sendNotification(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}



