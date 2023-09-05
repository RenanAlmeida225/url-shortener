package com.urlshortener.exceptions;

import java.time.Instant;

public record StandardException(Instant timestamp, int status, String error, String message, String path) {
}
