package com.urlshortener.dtos;

import java.time.LocalDateTime;

public record UrlResponseDto(String shortUrl, String longUrl, LocalDateTime limitDate) {
}
