package com.urlshortener.services.impl;

import com.urlshortener.Repositories.StatisticRepository;
import com.urlshortener.entities.Statistic;
import com.urlshortener.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public void save(Statistic statistic) {
        this.statisticRepository.save(statistic);
    }

    @Override
    public int getAmountOfAccess(String shortUrl) {
        return 0;
    }
}
