package com.urlshortener.services;

import com.urlshortener.entities.Url;

public interface StatisticService {
    void save(Url url);

    int getAmountOfAccess(String shortUrl);
}
