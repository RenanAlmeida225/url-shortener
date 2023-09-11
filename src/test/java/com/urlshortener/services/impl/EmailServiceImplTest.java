package com.urlshortener.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.exceptions.FailedSendMailException;
import com.urlshortener.services.UrlService;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UrlService urlService;

    private final String to = "any_email@mail.com";
    private final String urlDomain = "localhost:8080/";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(emailService, "to", this.to);
        ReflectionTestUtils.setField(emailService, "urlDomain", this.urlDomain);
    }

    @Test
    void sendEmail_ShouldThrowIfUrlNotFound() {
        String fullShortUrl = "localhost:8080/sjdT5";
        String message = "email is malicious";
        when(urlService.findUrl(anyString())).thenThrow(new EntityNotFoundException("url not found"));

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> emailService.sendEmail(fullShortUrl, message));
        assertEquals("url not found", thrown.getMessage());
        verify(urlService, times(1)).findUrl(anyString());
        verify(this.mailSender, times(0)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_ShouldFailOnSendEmail() {
        String fullShortUrl = "localhost:8080/sjdT5";
        String message = "email is malicious";
        doThrow(MailSendException.class).when(mailSender).send(any(SimpleMailMessage.class));
        when(urlService.findUrl(anyString())).thenReturn(any(UrlResponseDto.class));

        FailedSendMailException thrown = assertThrows(FailedSendMailException.class,
                () -> emailService.sendEmail(fullShortUrl, message));
        assertEquals("fail on send email", thrown.getMessage());
        verify(urlService, times(1)).findUrl(anyString());
        verify(this.mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_ShouldSendEmail() {
        String fullShortUrl = "localhost:8080/sjdT5";
        String message = "email is malicious";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(urlService.findUrl(anyString())).thenReturn(any(UrlResponseDto.class));

        emailService.sendEmail(fullShortUrl, message);
        verify(urlService, times(1)).findUrl(anyString());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
