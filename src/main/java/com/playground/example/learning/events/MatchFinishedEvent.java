package com.playground.example.learning.events;

import com.playground.example.learning.entity.Match;
import org.springframework.context.ApplicationEvent;

public class MatchFinishedEvent extends ApplicationEvent
{
    private final Match match;

    public MatchFinishedEvent(Object source, Match match) {
        super(source);
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }
}
