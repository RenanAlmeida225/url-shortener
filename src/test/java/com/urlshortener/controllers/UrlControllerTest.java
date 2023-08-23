package com.urlshortener.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UrlControllerTest {

    private final String urlDomain = "http://localhost:8080/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UrlService urlService;

    @Test
    void save_ShouldThrowIfShortUrlExists() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        SaveUrlDto data = new SaveUrlDto(longUrl);
        when(this.urlService.saveUrl(longUrl)).thenThrow(new RuntimeException("short url exists"));

        assertThatThrownBy(
                () -> mockMvc.perform(post("/url").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
        ).hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("short url exists");
    }

    @Test
    void save_ShouldSaveUrlAndReturnStatus201() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        SaveUrlDto data = new SaveUrlDto(longUrl);
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        when(this.urlService.saveUrl(any())).thenReturn(fullUrlShort);

        ResultMatcher resultMatcher = content().string(containsString(fullUrlShort));
        this.mockMvc.perform(post("/url").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isCreated())
                .andExpect(resultMatcher);

    }

    @Test
    void redirect_shouldThrowIfShortUrlNotFound() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        when(this.urlService.findUrl(shortUrl)).thenThrow(new RuntimeException("url not found"));

        assertThatThrownBy(
                () -> mockMvc.perform(get("/{shortUrl}", shortUrl).contentType(MediaType.APPLICATION_JSON))
        ).hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("url not found");
    }

    @Test
    void redirect_ShouldRedirectToLongUrl() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        when(this.urlService.findUrl(shortUrl)).thenReturn(longUrl);

        this.mockMvc.perform(get("/{shortUrl}", shortUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(longUrl));
    }
}
