package com.urlshortener.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.urlshortener.entities.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    @Modifying
    @Query(value = "DELETE FROM urls WHERE EXTRACT(DAY FROM(limit_date - current_timestamp)) <= 0", nativeQuery = true)
    void deleteUrlWithLimitDate();
}