package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.entities.Url;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.services.UrlService;
import com.urlshortener.utils.RandomCharacters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final RandomCharacters randomCharacters;
    @Value("${url.domain}")
    private String urlDomain;

    public UrlServiceImpl(UrlRepository urlRepository, RandomCharacters randomCharacters) {
        this.urlRepository = urlRepository;
        this.randomCharacters = randomCharacters;
    }

    @Override
    public UrlResponseDto generateUrl(String longUrl, int limitDays) {
        String shortUrl = randomCharacters.generate(5);
        LocalDateTime limitDate = LocalDateTime.now().plusDays(limitDays);
        Url url = new Url(longUrl, shortUrl, limitDate);
        this.urlRepository.save(url);
        return new UrlResponseDto(this.urlDomain + shortUrl, limitDate);
    }

    @Override
    public String findUrl(String shortUrl) {
        Url url = this.urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new EntityNotFoundException("url not found"));
        return url.getLongUrl();
    }
}
