package com.playground.example.learning.api.repository;

import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.repository.MatchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MatchRepositoryTests
{
    @Autowired
    private MatchRepository matchRepository;

    @Test
    public void saveMatchTest()
    {
        Match match = Match.builder()
            .matchDate("2023-10-01")
            .htPlayer(new Player(1L, "Home", "Player", "htPlayer"))
            .atPlayer(new Player(2L, "Away", "Player", "atPlayer"))
            .homeTeam(new Team(1L, "Home Team", "testSrcToLogo"))
            .awayTeam(new Team(2L, "Away Team", "testSrcToLogo"))
            .htScore(3)
            .atScore(2)
            .overTime(false)
            .htPoints(3)
            .atPoints(0)
            .isFinished(true)
            .series(null)
            .build();

        Match savedMatch = matchRepository.save(match);

        Assertions.assertNotNull(savedMatch);
        Assertions.assertNotNull(savedMatch.getId());
        Assertions.assertEquals("2023-10-01", savedMatch.getMatchDate());
        Assertions.assertEquals("htPlayer", savedMatch.getHtPlayer().getNickName());
        Assertions.assertEquals("atPlayer", savedMatch.getAtPlayer().getNickName());
        Assertions.assertEquals("Home Team", savedMatch.getHomeTeam().getName());
        Assertions.assertEquals("Away Team", savedMatch.getAwayTeam().getName());
        Assertions.assertEquals(3, savedMatch.getHtScore());
        Assertions.assertEquals(2, savedMatch.getAtScore());
        Assertions.assertFalse(savedMatch.getOverTime());
        Assertions.assertEquals(3, savedMatch.getHtPoints());
        Assertions.assertEquals(0, savedMatch.getAtPoints());
        Assertions.assertTrue(savedMatch.getIsFinished());
        Assertions.assertNull(savedMatch.getSeries());
    }

    @Test
    public void overtimePointsCountingTest()
    {
        Match match = Match.builder()
            .matchDate("2023-10-01")
            .htPlayer(new Player(1L, "Home", "Player", "htPlayer"))
            .atPlayer(new Player(2L, "Away", "Player", "atPlayer"))
            .homeTeam(new Team(1L, "Home Team", "testSrcToLogo"))
            .awayTeam(new Team(2L, "Away Team", "testSrcToLogo"))
            .htScore(3)
            .atScore(2)
            .overTime(true)
            .htPoints(4)
            .atPoints(3)
            .isFinished(true)
            .series(null)
            .build();

        Match savedMatch = matchRepository.save(match);

        Assertions.assertNotNull(savedMatch);
        Assertions.assertEquals(4, savedMatch.getHtPoints());
        Assertions.assertEquals(3, savedMatch.getAtPoints());
    }
}
