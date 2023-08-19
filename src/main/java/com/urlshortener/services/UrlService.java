package com.urlshortener.services;

public interface UrlService {
    String saveUrl(String longUrl);

    String findUrl(String shortUrl);
}
