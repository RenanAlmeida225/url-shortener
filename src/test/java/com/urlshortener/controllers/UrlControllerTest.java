package com.urlshortener.controllers;

import com.urlshortener.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UrlControllerTest {

    private final String urlDomain = "http://localhost:8080/";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UrlService urlService;

    @Test
    void save_ShouldThrowIfShortUrlExists() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        when(this.urlService.saveUrl(longUrl)).thenThrow(new RuntimeException("short url exists"));

        assertThatThrownBy(
                () -> mockMvc.perform(post("/url").content(longUrl))
        ).hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("short url exists");
    }

    @Test
    void save_ShouldSaveUrlAndReturnStatus201() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        when(this.urlService.saveUrl(any())).thenReturn(fullUrlShort);

        this.mockMvc.perform(post("/url").content(longUrl))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(fullUrlShort)));
    }
}
