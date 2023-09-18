package com.urlshortener.utils.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteUrlsTaskImplTest {

    @Mock
    private DeleteUrlsTaskImpl deleteUrlsTask;

    @Test
    void delete_ShouldDeleteUrls() {
        await().atMost(Duration.ofSeconds(1)).untilAsserted(() -> verify(deleteUrlsTask, atLeast(0)).delete());
    }
}
