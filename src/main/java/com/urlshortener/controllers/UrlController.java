package com.urlshortener.controllers;

import com.urlshortener.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("url")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody String longUrl) {
        String shortUrl = this.urlService.saveUrl(longUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }
}
