package com.urlshortener.Repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.urlshortener.entities.Url;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto= create",
        "spring.datasource.url= jdbc:postgresql://localhost:5432/testdb",
        "spring.datasource.username= postgres",
        "spring.datasource.password= p@ssw0rd",
        "spring.jpa.properties.hibernate.show_sql=true",
        "spring.jpa.properties.hibernate.format_sql=true"
})
public class UrlRepositoryIT {

    @Autowired
    private UrlRepository urlRepository;
    private MockedStatic<LocalDateTime> localDateTimeMocked;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        this.now = LocalDateTime.of(2022, 10, 1, 10, 0);
        localDateTimeMocked = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
        localDateTimeMocked.when(LocalDateTime::now).thenReturn(this.now);
    }

    @Test
    void deleteWithLimitDate_ShouldDeleteUrls() {
        String longUrl = "https://www.originalUrl.com/this-is-an-very-long-url";
        String shortUrl = "fkt8y";
        int limitDays = 15;
        LocalDateTime limitDate = this.now.minusDays(limitDays);
        Url url = new Url(longUrl, shortUrl, limitDate);
        this.urlRepository.save(url);

        this.urlRepository.deleteUrlWithLimitDate();

        List<Url> urls = this.urlRepository.findAll();
        assertEquals(true, urls.isEmpty());
    }
}
