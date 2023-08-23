package com.urlshortener.dtos;

import jakarta.validation.constraints.NotBlank;

public record SaveUrlDto(@NotBlank String longUrl) {
}
