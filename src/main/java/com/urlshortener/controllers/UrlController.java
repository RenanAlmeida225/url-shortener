package com.urlshortener.controllers;

import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class UrlController {

    private final UrlService urlService;

    @PostMapping("url")
    public ResponseEntity<String> save(@RequestBody SaveUrlDto data) {
        String shortUrl = this.urlService.saveUrl(data.longUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping("{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        String originalUrl = this.urlService.findUrl(shortUrl);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(originalUrl);
        return redirectView;
    }
}
