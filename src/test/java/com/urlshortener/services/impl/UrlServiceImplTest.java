package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Url;
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

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(urlService, "urlDomain", this.urlDomain);
    }

    @Test
    void saveUrl_ShouldThrowAnException() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        Url url = new Url(longUrl, shortUrl);
        when(this.randomCharacters.generate(5)).thenReturn(shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> this.urlService.saveUrl(longUrl));
        assertEquals("short url exists", thrown.getMessage());
        verify(this.urlRepository, never()).save(any());
    }

    @Test
    void saveUrl_ShouldSaveUrlAndReturnShortUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        String fullShortUrl = this.urlDomain + shortUrl;
        when(this.randomCharacters.generate(5)).thenReturn(shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String saved = this.urlService.saveUrl(longUrl);
        assertEquals(fullShortUrl, saved);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
        verify(this.urlRepository, times(1)).save(any());
    }

    @Test
    void findUrl_ShouldThrowIfUrlNotFound() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
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
        Url url = new Url(longUrl, shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        String originalUrl = this.urlService.findUrl(shortUrl);

        assertEquals(longUrl, originalUrl);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }
}
