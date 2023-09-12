package com.urlshortener.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.entities.Url;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.utils.RandomCharacters;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
    private final String urlDomain = "http://localhost:8080/";
    MockedStatic<LocalDateTime> localDateTimeMocked;
    private LocalDateTime now;
    @InjectMocks
    private UrlServiceImpl urlService;
    @Mock
    private UrlRepository urlRepository;
    @Mock
    private RandomCharacters randomCharacters;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(urlService, "urlDomain", this.urlDomain);
        localDateTimeMocked = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
        this.now = LocalDateTime.of(2023, 10, 1, 10, 0);
        localDateTimeMocked.when(LocalDateTime::now).thenReturn(now);
    }

    @AfterEach
    void reset() {
        localDateTimeMocked.close();
    }

    @Test
    void generateUrl_ShouldSaveUrlAndReturnShortUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 5;
        String fullShortUrl = this.urlDomain + shortUrl;
        LocalDateTime limitDate = this.now.plusDays(limitDays);
        UrlResponseDto response = new UrlResponseDto(fullShortUrl, longUrl, limitDate);
        when(this.randomCharacters.generate(5)).thenReturn(shortUrl);

        UrlResponseDto saved = this.urlService.generateUrl(longUrl, limitDays);

        verify(this.urlRepository, times(1)).save(any());
        assertEquals(response, saved);
    }

    @Test
    void findUrl_ShouldThrowIfUrlNotFound() {
        String shortUrl = "fkt8y";
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(EntityNotFoundException.class, () -> this.urlService.findUrl(shortUrl));
        assertEquals("url not found", thrown.getMessage());
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }

    @Test
    void findUrl_ShouldReturnTheOriginalUrlWithFullShortUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 15;
        String fullShortUrl = this.urlDomain + shortUrl;
        LocalDateTime limitDate = this.now.plusDays(limitDays);
        Url url = new Url(longUrl, shortUrl, limitDate);
        UrlResponseDto responseDto = new UrlResponseDto(fullShortUrl, longUrl, limitDate);
        when(this.urlRepository.findByShortUrl(anyString())).thenReturn(Optional.of(url));

        UrlResponseDto res = this.urlService.findUrl(fullShortUrl);

        assertEquals(responseDto, res);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }

    @Test
    void findUrl_ShouldReturnTheOriginalUrlWithShortUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 15;
        String fullShortUrl = this.urlDomain + shortUrl;
        LocalDateTime limitDate = this.now.plusDays(limitDays);
        Url url = new Url(longUrl, shortUrl, limitDate);
        UrlResponseDto responseDto = new UrlResponseDto(fullShortUrl, longUrl, limitDate);
        when(this.urlRepository.findByShortUrl(anyString())).thenReturn(Optional.of(url));

        UrlResponseDto res = this.urlService.findUrl(shortUrl);

        assertEquals(responseDto, res);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }
}
