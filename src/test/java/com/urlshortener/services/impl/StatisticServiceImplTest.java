package com.urlshortener.services.impl;

import com.urlshortener.Repositories.StatisticRepository;
import com.urlshortener.entities.Statistic;
import com.urlshortener.entities.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Mock
    private StatisticRepository statisticRepository;

    @Test
    void save_ShouldSaveStatistic() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        Url url = new Url(longUrl, shortUrl);
        Statistic statistic = new Statistic(url);

        this.statisticService.save(statistic);

        verify(this.statisticRepository, times(1)).save(any());
    }

    @Test
    void getAmountOfAccess() {
    }
}
