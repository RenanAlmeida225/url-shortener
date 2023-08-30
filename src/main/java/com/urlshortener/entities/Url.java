package com.urlshortener.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int limitDays;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createAt;

    public Url(String longUrl, String shortUrl, int limitDays) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.limitDays = limitDays;
        this.createAt = LocalDateTime.now();
    }
}
