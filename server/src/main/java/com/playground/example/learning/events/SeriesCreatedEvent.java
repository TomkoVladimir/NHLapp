package com.playground.example.learning.events;

import com.playground.example.learning.entity.Series;
import org.springframework.context.ApplicationEvent;

public class SeriesCreatedEvent extends ApplicationEvent
{
    private final Series series;

    public SeriesCreatedEvent(Object source, Series series) {
        super(source);
        this.series = series;
    }

    public Series getSeries() {
        return series;
    }
}
