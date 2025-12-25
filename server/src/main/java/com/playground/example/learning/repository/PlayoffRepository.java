package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Playoff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayoffRepository extends JpaRepository<Playoff, Long>
{
    @Query("SELECT p FROM Playoff p LEFT JOIN FETCH p.series WHERE p.id = :id")
    Optional<Playoff> findByIdWithSeries(@Param("id") Long id);
}
