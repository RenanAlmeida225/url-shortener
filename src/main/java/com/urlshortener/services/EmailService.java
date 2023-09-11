package com.urlshortener.services;

public interface EmailService {
    void sendEmail(String shortUrl, String message);
}
