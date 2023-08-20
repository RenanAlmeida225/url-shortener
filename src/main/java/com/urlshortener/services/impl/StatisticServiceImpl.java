package com.urlshortener.services.impl;

import com.urlshortener.entities.Statistic;
import com.urlshortener.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {
    @Override
    public void saveStatisticService(Statistic statistic) {

    }

    @Override
    public int getAmountOfAccess(String shortUrl) {
        return 0;
    }
}
