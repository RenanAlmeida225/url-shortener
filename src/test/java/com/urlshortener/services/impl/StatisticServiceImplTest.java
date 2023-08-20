package com.urlshortener.services.impl;

import com.urlshortener.Repositories.StatisticRepository;
import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Statistic;
import com.urlshortener.entities.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Mock
    private StatisticRepository statisticRepository;

    @Mock
    private UrlRepository urlRepository;

    @Test
    void save_ShouldSaveStatistic() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        Url url = new Url(longUrl, shortUrl);

        this.statisticService.save(url);

        verify(this.statisticRepository, times(1)).save(any());
    }

    @Test
    void getAmountOfAccess_ShouldThrowIfUrlNotFound() {
        String shortUrl = "fkt8y";
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> this.statisticService.getAmountOfAccess(shortUrl));
        assertEquals("url not found", thrown.getMessage());
        verify(this.statisticRepository, times(0)).findByUrl(any());
        verifyNoInteractions(this.statisticRepository);
    }

    @Test
    void getAmountOfAccess_ShouldThrowIfStatisticNotFound() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        Url url = new Url(longUrl, shortUrl);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));
        when(this.statisticRepository.findByUrl(url)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> this.statisticService.getAmountOfAccess(shortUrl));
        assertEquals("statistic not found", thrown.getMessage());
        verify(this.statisticRepository, times(1)).findByUrl(any());
    }

    @Test
    void getAmountOfAccess_ShouldReturnAmountOfAccess() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int amountOfAccess = 10;
        Url url = new Url(longUrl, shortUrl);
        Statistic statistic = new Statistic(url);
        statistic.setAmountOfAccess(amountOfAccess);
        when(this.urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));
        when(this.statisticRepository.findByUrl(url)).thenReturn(Optional.of(statistic));

        int output = this.statisticService.getAmountOfAccess(shortUrl);

        verify(this.statisticRepository, times(1)).findByUrl(any());
        verify(this.urlRepository, times(1)).findByShortUrl(any());
        assertEquals(amountOfAccess, output);

    }
}
