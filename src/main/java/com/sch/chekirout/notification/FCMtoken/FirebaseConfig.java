package com.sch.chekirout.notification.FCMtoken;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
@EnableScheduling
public class FirebaseConfig {

    @Value("${firebase.service-account-key}")
    private String serviceAccountKeyJson;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        // JSON 문자열을 로그로 확인 (앞부분만 출력)
        log.info("Firebase Service Account Key (partial): {}",
                serviceAccountKeyJson.substring(0, Math.min(100, serviceAccountKeyJson.length())) + "...");

        // JSON 문자열을 InputStream으로 변환
        InputStream serviceAccount = new ByteArrayInputStream(serviceAccountKeyJson.getBytes());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}

