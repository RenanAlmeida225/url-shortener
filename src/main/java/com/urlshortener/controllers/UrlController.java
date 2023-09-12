package com.urlshortener.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.urlshortener.dtos.ReportMessageDto;
import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.services.EmailService;
import com.urlshortener.services.UrlService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class UrlController {

    private final UrlService urlService;
    private final EmailService emailService;

    public UrlController(UrlService urlService, EmailService emailService) {
        this.urlService = urlService;
        this.emailService = emailService;
    }

    @PostMapping("url")
    public ResponseEntity<UrlResponseDto> save(@RequestBody @Valid SaveUrlDto data) {
        UrlResponseDto shortUrl = this.urlService.generateUrl(data.longUrl(), data.limitDays());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping("url/unshortener")
    public ResponseEntity<UrlResponseDto> getOriginalUrl(@RequestParam String shortUrl) {
        UrlResponseDto url = this.urlService.findUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }

    @PostMapping("url/report")
    public ResponseEntity<String> report(@RequestBody @Valid ReportMessageDto data) {
        this.emailService.sendEmail(data.shortUrl(), data.message());
        return ResponseEntity.status(HttpStatus.OK).body("email sent");
    }

    @GetMapping("{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        UrlResponseDto responseDto = this.urlService.findUrl(shortUrl);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(responseDto.longUrl());
        return redirectView;
    }
}
