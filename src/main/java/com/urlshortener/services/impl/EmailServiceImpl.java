package com.urlshortener.services.impl;

import org.springframework.stereotype.Service;

import com.urlshortener.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String shortUrl, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmail'");
    }

}
