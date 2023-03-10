package com.spreecosmetics.api.v1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${client.host}")
    private String client;

    public void sendResetPasswordMail(String toEmail, String names, String activationCodes) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("premugisha64@gmail.com");
        message.setTo(toEmail);
        message.setText("Dear " + names + "!\n" +
                "\n" +
                "You've requested to reset password to Spree Cosmetics, " +
                "Click on the link below to reset your account password\n" +
                "This link expires in 2 hours.\n" +
                client + "/auth/reset-password/" + activationCodes +
                "\n" +
                "If you have any questions, send us an email precieux@support.com.\n" +
                "\n" +
                "We’re glad you’re here!\n" +
                "\n");
        message.setSubject("SPREE Cosmetics Password Reset");
        mailSender.send(message);
    }
}