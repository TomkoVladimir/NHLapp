package com.playground.example.learning.service.impl;

import com.playground.example.learning.entity.Match;
import com.playground.example.learning.events.MatchFinishedEvent;
import com.playground.example.learning.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SeriesMatchHandler
{
    @Autowired
    private SeriesService seriesService;

    @EventListener
    public void handleMatchFinished(MatchFinishedEvent event)
    {
        Match match = event.getMatch();
        if(match.getSeries() != null)
        {
            seriesService.updateSeriesAfterMatch(match);
        }
    }
}
