package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Url;
import com.urlshortener.services.StatisticService;
import com.urlshortener.services.UrlService;
import com.urlshortener.utils.RandomCharacters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final RandomCharacters randomCharacters;
    private final StatisticService statisticService;
    @Value("${url.domain}")
    private String urlDomain;

    @Override
    public String saveUrl(String longUrl) {
        String shortUrl = randomCharacters.generate(5);
        if (this.urlRepository.findByShortUrl(shortUrl).isPresent()) throw new RuntimeException("short url exists");
        Url url = new Url(longUrl, shortUrl);
        this.urlRepository.save(url);
        this.statisticService.save(url);
        return this.urlDomain + shortUrl;
    }

    @Override
    public String findUrl(String shortUrl) {
        Url url = this.urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new RuntimeException("url not found"));
        return url.getLongUrl();
    }
}
