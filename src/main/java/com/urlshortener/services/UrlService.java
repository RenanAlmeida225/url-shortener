package com.urlshortener.services;

import com.urlshortener.dtos.UrlResponseDto;

public interface UrlService {
    UrlResponseDto generateUrl(String longUrl, int limitDays);

    String findUrl(String shortUrl);
}
