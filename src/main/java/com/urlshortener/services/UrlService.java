package com.urlshortener.services;

import com.urlshortener.dtos.UrlResponseDto;

public interface UrlService {
    UrlResponseDto generateUrl(String longUrl, int limitDays);

    UrlResponseDto findUrl(String shortUrl);
}
