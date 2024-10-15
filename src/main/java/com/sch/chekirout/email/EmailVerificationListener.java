//package com.sch.chekirout.email;
//
//import com.sch.chekirout.email.Service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EmailVerificationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
//
//    private final EmailService emailService;
//
//    @Autowired
//    public EmailVerificationListener(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    @Override
//    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
//        String email = event.getUser().getEmail();
//        String token = event.getToken();
//        emailService.sendVerificationEmail(email, token);
//    }
//}