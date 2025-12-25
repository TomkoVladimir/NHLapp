package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long>
{
}

