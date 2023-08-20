package com.urlshortener.services.impl;

import com.urlshortener.Repositories.StatisticRepository;
import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.entities.Statistic;
import com.urlshortener.entities.Url;
import com.urlshortener.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;
    private final UrlRepository urlRepository;

    @Override
    public void save(Url url) {
        Statistic statistic = new Statistic(url);
        this.statisticRepository.save(statistic);
    }

    @Override
    public int getAmountOfAccess(String shortUrl) {
        Url url = this.urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new RuntimeException("url not found"));
        Statistic statistic = this.statisticRepository.findByUrl(url).orElseThrow(() -> new RuntimeException("statistic not found"));
        return statistic.getAmountOfAccess();
    }
}
