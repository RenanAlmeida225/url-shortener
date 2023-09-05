package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Url;
import com.urlshortener.services.UrlService;
import com.urlshortener.utils.RandomCharacters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public String generateUrl(String longUrl, int limitDays) {
        String shortUrl = randomCharacters.generate(5);
        if (this.urlRepository.findByShortUrl(shortUrl).isPresent())
            throw new RuntimeException("short url exists");
        Url url = new Url(longUrl, shortUrl, limitDays);
        this.urlRepository.save(url);
        return this.urlDomain + shortUrl;
    }

    @Override
    public String findUrl(String shortUrl) {
        Url url = this.urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new RuntimeException("url not found"));
        return url.getLongUrl();
    }
}
