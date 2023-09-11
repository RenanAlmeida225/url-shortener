package com.urlshortener.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.urlshortener.exceptions.FailedSendMailException;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    private final String to = "any_email@mail.com";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(emailService, "to", this.to);
    }

    @Test
    void testSendEmail_ShouldFailOnSendEmail() {
        String fullShortUrl = "localhost:8080/sjdT5";
        String message = "email is malicious";
        doThrow(MailSendException.class).when(mailSender).send(any(SimpleMailMessage.class));

        FailedSendMailException thrown = assertThrows(FailedSendMailException.class,
                () -> emailService.sendEmail(fullShortUrl, message));
        assertEquals("fail on send email", thrown.getMessage());
        verify(this.mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_ShouldSendEmail() {
        String fullShortUrl = "localhost:8080/sjdT5";
        String message = "email is malicious";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(fullShortUrl, message);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
