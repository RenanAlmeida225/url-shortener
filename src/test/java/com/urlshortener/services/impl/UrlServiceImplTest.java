package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Url;
import com.urlshortener.services.StatisticService;
import com.urlshortener.utils.RandomCharacters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
    private final String urlDomain = "http://localhost:8080/";
    @InjectMocks
    private UrlServiceImpl urlService;
    @Mock
    private UrlRepository urlRepository;
    @Mock
    private RandomCharacters randomCharacters;
    @Mock
    private StatisticService statisticService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(urlService, "urlDomain", this.urlDomain);
    }

    @Test
    void generateUrl_ShouldThrowAnExceptionIfShortUrlExists() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 5;
        Url url = new Url(longUrl, shortUrl, limitDays);
        when(this.randomCharacters.generate(5)).thenReturn(shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        verify(this.urlRepository, never()).save(any());
        verifyNoInteractions(this.statisticService);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> this.urlService.generateUrl(longUrl, limitDays));
        assertEquals("short url exists", thrown.getMessage());
    }

    @Test
    void generateUrl_ShouldSaveUrlAndReturnShortUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 5;
        String fullShortUrl = this.urlDomain + shortUrl;
        when(this.randomCharacters.generate(5)).thenReturn(shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String saved = this.urlService.generateUrl(longUrl, limitDays);

        verify(this.urlRepository, times(1)).findByShortUrl(any());
        verify(this.urlRepository, times(1)).save(any());
        verify(this.statisticService, times(1)).save(any());
        assertEquals(fullShortUrl, saved);
    }

    @Test
    void findUrl_ShouldThrowIfUrlNotFound() {
        String shortUrl = "fkt8y";
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> this.urlService.findUrl(shortUrl));
        assertEquals("url not found", thrown.getMessage());
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }

    @Test
    void findUrl_ShouldReturnTheOriginalUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 5;
        Url url = new Url(longUrl, shortUrl, limitDays);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        String originalUrl = this.urlService.findUrl(shortUrl);

        assertEquals(longUrl, originalUrl);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }
}
