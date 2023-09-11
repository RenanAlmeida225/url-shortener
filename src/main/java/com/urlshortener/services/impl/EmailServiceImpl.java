package com.urlshortener.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.urlshortener.exceptions.FailedSendMailException;
import com.urlshortener.services.EmailService;
import com.urlshortener.services.UrlService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UrlService urlService;

    @Value("${spring.mail.username}")
    private String to;
    @Value("${url.domain}")
    private String urlDomain;

    public EmailServiceImpl(JavaMailSender mailSender, UrlService urlService) {
        this.mailSender = mailSender;
        this.urlService = urlService;
    }

    @Override
    public void sendEmail(String shortUrl, String message) {
        this.urlService.findUrl(shortUrl.split(this.urlDomain)[1]);
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
