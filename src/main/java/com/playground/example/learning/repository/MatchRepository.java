package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>
{
    int countBySeries(Series series);


}
