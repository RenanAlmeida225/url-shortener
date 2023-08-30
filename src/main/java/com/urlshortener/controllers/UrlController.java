package com.urlshortener.controllers;

import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("url")
    public ResponseEntity<String> save(@RequestBody SaveUrlDto data) {
        String shortUrl = this.urlService.generateUrl(data.longUrl(), data.limitDays());
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
