package com.urlshortener.services;

import com.urlshortener.entities.Statistic;

public interface StatisticService {
    void saveStatisticService(Statistic statistic);

    int getAmountOfAccess(String shortUrl);
}
