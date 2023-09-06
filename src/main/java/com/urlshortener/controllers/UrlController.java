package com.urlshortener.controllers;

import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.services.UrlService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UrlResponseDto> save(@RequestBody @Valid SaveUrlDto data) {
        UrlResponseDto shortUrl = this.urlService.generateUrl(data.longUrl(), data.limitDays());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping("url")
    public ResponseEntity<UrlResponseDto> getOriginalUrl(@RequestParam String shortUrl) {
        UrlResponseDto url = this.urlService.findUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }


    @GetMapping("{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        UrlResponseDto responseDto = this.urlService.findUrl(shortUrl);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(responseDto.longUrl());
        return redirectView;
    }
}
