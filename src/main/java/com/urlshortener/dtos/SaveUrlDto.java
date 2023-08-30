package com.urlshortener.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveUrlDto(@NotBlank String longUrl, @Min(0) @Max(10) int limitDays) {
}
