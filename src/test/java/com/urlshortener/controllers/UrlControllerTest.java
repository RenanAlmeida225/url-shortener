package com.urlshortener.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.dtos.ReportMessageDto;
import com.urlshortener.dtos.SaveUrlDto;
import com.urlshortener.dtos.UrlResponseDto;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.exceptions.FailedSendMailException;
import com.urlshortener.exceptions.StandardException;
import com.urlshortener.services.EmailService;
import com.urlshortener.services.UrlService;

@WebMvcTest
class UrlControllerTest {
    private final String urlDomain = "http://localhost:8080/";
    MockedStatic<LocalDateTime> localDateTimeMocked;
    MockedStatic<Instant> instantMocked;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UrlService urlService;

    @MockBean
    private EmailService emailService;

    private LocalDateTime now;

    @BeforeEach
    void setup() {
        instantMocked = mockStatic(Instant.class, CALLS_REAL_METHODS);
        Instant now = Instant.parse("2023-10-01T10:00:00.00Z");
        instantMocked.when(Instant::now).thenReturn(now);

        localDateTimeMocked = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
        this.now = LocalDateTime.of(2023, 10, 1, 10, 0);
        localDateTimeMocked.when(LocalDateTime::now).thenReturn(this.now);
    }

    @AfterEach
    void reset() {
        instantMocked.close();
        localDateTimeMocked.close();
    }

    @Test
    void save_ShouldThrowIfLongUrlIsBlank() throws Exception {
        SaveUrlDto data = new SaveUrlDto("", 10);
        StandardException standardException = new StandardException(Instant.now(), 400, "method argument not valid",
                "[longUrl: 'must not be blank']", "/url");
        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void save_ShouldThrowIfLimitDaysLessThan1() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        SaveUrlDto data = new SaveUrlDto(longUrl, 0);
        StandardException standardException = new StandardException(Instant.now(), 400, "method argument not valid",
                "[limitDays: 'must be greater than or equal to 1']", "/url");

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void save_ShouldThrowIfLimitDaysGreaterThan30() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        SaveUrlDto data = new SaveUrlDto(longUrl, 31);
        StandardException standardException = new StandardException(Instant.now(), 400, "method argument not valid",
                "[limitDays: 'must be less than or equal to 30']", "/url");

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void save_ShouldSaveUrlAndReturnStatus201() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        int limitDays = 15;
        SaveUrlDto data = new SaveUrlDto(longUrl, limitDays);
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        LocalDateTime limitDate = now.plusDays(limitDays);
        UrlResponseDto responseDto = new UrlResponseDto(fullUrlShort, longUrl, limitDate);
        when(this.urlService.generateUrl(any(), anyInt())).thenReturn(responseDto);

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(responseDto));
        this.mockMvc.perform(post("/url")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isCreated())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void getOriginalUrl_ShouldThrowIfShortUrlNotExist() throws Exception {
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        StandardException standardException = new StandardException(Instant.now(), 404, "entity not found",
                "url not found", "/url/unshortener");
        when(this.urlService.findUrl(anyString())).thenThrow(new EntityNotFoundException("url not found"));

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(get("/url/unshortener").param("shortUrl", fullUrlShort))
                .andExpect(status().isNotFound())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void getOriginalUrl_ShouldReturnResponse() throws Exception {
        String shortUrl = "fkt8y";
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        int limitDays = 15;
        LocalDateTime limitDate = now.plusDays(limitDays);
        String fullUrlShort = this.urlDomain + shortUrl;
        UrlResponseDto responseDto = new UrlResponseDto(fullUrlShort, longUrl, limitDate);
        when(this.urlService.findUrl(anyString())).thenReturn(responseDto);

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(responseDto));
        mockMvc.perform(get("/url/unshortener").param("shortUrl", shortUrl))
                .andExpect(status().isOk())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void report_ShouldReturnStatus400IfShortUrlisBlank() throws Exception {
        ReportMessageDto data = new ReportMessageDto("", "email is malicious");
        StandardException standardException = new StandardException(Instant.now(), 400, "method argument not valid",
                "[shortUrl: 'must not be blank']", "/url/report");
        doThrow(new EntityNotFoundException("url not found")).when(emailService).sendEmail(anyString(), anyString());

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void report_ShouldReturnStatus400IfMessageisBlank() throws Exception {
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        ReportMessageDto data = new ReportMessageDto(fullUrlShort, "");
        StandardException standardException = new StandardException(Instant.now(), 400, "method argument not valid",
                "[message: 'must not be blank']", "/url/report");
        doThrow(new EntityNotFoundException("url not found")).when(emailService).sendEmail(anyString(), anyString());

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void report_ShouldReturnStatus404IfUrlNotFound() throws Exception {
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        ReportMessageDto data = new ReportMessageDto(fullUrlShort, "email is malicious");
        StandardException standardException = new StandardException(Instant.now(), 404, "entity not found",
                "url not found", "/url/report");
        doThrow(new EntityNotFoundException("url not found")).when(emailService).sendEmail(anyString(), anyString());

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isNotFound())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void report_ShouldReturnStatus400ifEmailSendFail() throws Exception {
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        ReportMessageDto data = new ReportMessageDto(fullUrlShort, "email is malicious");
        StandardException standardException = new StandardException(Instant.now(), 400, "send email fail",
                "fail on send email", "/url/report");
        doThrow(new FailedSendMailException("fail on send email")).when(emailService).sendEmail(anyString(),
                anyString());

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(post("/url/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isBadRequest())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void report_ShouldReturnStatus200ifEmailSent() throws Exception {
        String shortUrl = "fkt8y";
        String fullUrlShort = this.urlDomain + shortUrl;
        ReportMessageDto data = new ReportMessageDto(fullUrlShort, "email is malicious");
        doNothing().when(emailService).sendEmail(anyString(), anyString());

        mockMvc.perform(post("/url/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(data)))
                .andExpect(status().isOk())
                .andExpect(content().string("email sent"))
                .andReturn();
    }

    @Test
    void redirect_shouldThrowIfShortUrlNotFound() throws Exception {
        String shortUrl = "fkt8y";
        StandardException standardException = new StandardException(Instant.now(), 404, "entity not found",
                "url not found", "/" + shortUrl);
        when(this.urlService.findUrl(shortUrl)).thenThrow(new EntityNotFoundException("url not found"));

        ResultMatcher resultMatcher = content().json(mapper.writeValueAsString(standardException));
        mockMvc.perform(get("/{shortUrl}", shortUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(resultMatcher)
                .andReturn();
    }

    @Test
    void redirect_ShouldRedirectToLongUrl() throws Exception {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 15;
        String fullUrlShort = this.urlDomain + shortUrl;
        LocalDateTime limitDate = now.plusDays(limitDays);
        UrlResponseDto responseDto = new UrlResponseDto(fullUrlShort, longUrl, limitDate);
        when(this.urlService.findUrl(shortUrl)).thenReturn(responseDto);

        this.mockMvc.perform(get("/{shortUrl}", shortUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(longUrl));
    }
}
