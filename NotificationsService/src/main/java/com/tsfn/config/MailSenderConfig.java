//package com.tsfn.config;
//
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//@Configuration
//public class MailSenderConfig {
//
//    @Value("${SPRING_MAIL_USERNAME}")
//    private String emailUsername;
//
//    @Value("${SPRING_MAIL_PASSWORD}")
//    private String emailPassword;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername(emailUsername);
//        mailSender.setPassword(emailPassword);
//
//        Properties properties = mailSender.getJavaMailProperties();
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
//
//        return mailSender;
//    }
//}
