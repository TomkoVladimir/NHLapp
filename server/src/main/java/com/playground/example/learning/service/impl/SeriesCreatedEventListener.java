package com.playground.example.learning.service.impl;

import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Series;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.events.SeriesCreatedEvent;
import com.playground.example.learning.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeriesCreatedEventListener {

    private final MatchServiceImpl matchService;
    private final MatchRepository matchRepository;

    public SeriesCreatedEventListener(MatchServiceImpl matchService, MatchRepository matchRepository) {
        this.matchService = matchService;
        this.matchRepository = matchRepository;
    }

    @EventListener
    @Transactional
    public void handleSeriesCreated(SeriesCreatedEvent event) {
        Series series = event.getSeries();
        Player p1 = series.getPlayerOne();
        Player p2 = series.getPlayerTwo();
        Team t1 = series.getTeamOne();
        Team t2 = series.getTeamTwo();

        List<Match> matches = new ArrayList<>();
        matches.add(matchService.createSeriesMatch(p1, p2, t1, t2, series));
        matches.add(matchService.createSeriesMatch(p1, p2, t1, t2, series));
        matches.add(matchService.createSeriesMatch(p2, p1, t2, t1, series));
        matches.add(matchService.createSeriesMatch(p2, p1, t2, t1, series));

        matchRepository.saveAll(matches);
    }
}
