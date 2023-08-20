package com.urlshortener.services.impl;

import com.urlshortener.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {
    @Override
    public int getAmountOfAccess(String shortUrl) {
        return 0;
    }
}
