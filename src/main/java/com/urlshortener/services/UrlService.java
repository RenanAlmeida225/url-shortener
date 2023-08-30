package com.urlshortener.services;

public interface UrlService {
    String generateUrl(String longUrl, int limitDays);

    String findUrl(String shortUrl);
}
