package com.urlshortener.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.urlshortener.exceptions.FailedSendMailException;
import com.urlshortener.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String to;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String shortUrl, String message) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(this.to);
            email.setSubject(shortUrl);
            email.setText(message);

            this.mailSender.send(email);
        } catch (MailException e) {
            throw new FailedSendMailException("fail on send email");
        }
    }

}
