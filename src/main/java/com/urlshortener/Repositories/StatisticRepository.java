package com.urlshortener.Repositories;

import com.urlshortener.entities.Statistic;
import com.urlshortener.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findByUrl(Url url);
}
