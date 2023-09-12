package com.urlshortener.dtos;

import jakarta.validation.constraints.NotBlank;

public record ReportMessageDto(@NotBlank String shortUrl, @NotBlank String message) {

}
