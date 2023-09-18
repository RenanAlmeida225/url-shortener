package com.urlshortener.utils.impl;

import com.urlshortener.Repositories.UrlRepository;
import com.urlshortener.utils.DeleteUrlsTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeleteUrlsTaskImpl implements DeleteUrlsTask {

    private final UrlRepository urlRepository;

    public DeleteUrlsTaskImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void delete() {
        this.urlRepository.deleteUrlWithLimitDate();
    }

}
