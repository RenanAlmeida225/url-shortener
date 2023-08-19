package com.urlshortener.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "statistics")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int amountOfAccess;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime lastAccess;
    @OneToOne(targetEntity = Url.class, fetch = FetchType.EAGER)
    private Url url;

    public Statistic(Url url) {
        this.amountOfAccess = 0;
        this.lastAccess = null;
        this.url = url;
    }
}
