package com.urlshortener.services.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.entities.Url;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.utils.RandomCharacters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        UrlResponseDto response = new UrlResponseDto(fullShortUrl, limitDate);
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
    void findUrl_ShouldReturnTheOriginalUrl() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 15;
        localDateTimeMocked = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
        LocalDateTime now = LocalDateTime.of(2023, 10, 1, 10, 0);
        localDateTimeMocked.when(LocalDateTime::now).thenReturn(now);
        LocalDateTime limitDate = now.plusDays(limitDays);
        Url url = new Url(longUrl, shortUrl, limitDate);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));
        System.out.println("(url)==> " + url);
        String originalUrl = this.urlService.findUrl(shortUrl);

        assertEquals(longUrl, originalUrl);
        verify(this.urlRepository, times(1)).findByShortUrl(any());
    }
}
