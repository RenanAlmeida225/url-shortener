package com.urlshortener.services;

import com.urlshortener.entities.Statistic;

public interface StatisticService {
    void save(Statistic statistic);

    int getAmountOfAccess(String shortUrl);
}
