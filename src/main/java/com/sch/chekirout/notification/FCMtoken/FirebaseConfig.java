package com.sch.chekirout.notification.FCMtoken;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableScheduling
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        // ClassPathResource를 사용하여 resources 폴더의 JSON 파일을 읽음
        ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");

        // InputStream으로 FirebaseOptions 초기화
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}

