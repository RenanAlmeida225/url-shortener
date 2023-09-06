package com.urlshortener.dtos;

import java.time.LocalDateTime;

public record UrlResponseDto(String shortUrl, LocalDateTime limitDate) {
}
