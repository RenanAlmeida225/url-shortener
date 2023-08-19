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
    private String oldUrl;
    private String shortUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createAt;

    public Url(String oldUrl, String shortUrl) {
        this.oldUrl = oldUrl;
        this.shortUrl = shortUrl;
        this.createAt = LocalDateTime.now();
    }
}
