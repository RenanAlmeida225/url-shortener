package com.urlshortener.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String longUrl;
    private String shortUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createAt;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime limitDate;

    public Url(String longUrl, String shortUrl, LocalDateTime limitDate) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createAt = LocalDateTime.now();
        this.limitDate = limitDate;
    }
}
