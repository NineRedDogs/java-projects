package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Time {
    final private LocalDateTime t1;

    public Time() {
        t1 = LocalDateTime.now();
    }

    public Time(LocalDateTime time) {
        this.t1 = time;
    }

    public LocalDateTime getTime() {
        return t1;
    }

    public long diffMins(Time t) throws TimeException {
        if (t1 == null || t == null) {
            throw new TimeException("need 2 times to calculate difference");
        }
        long diffMins = ChronoUnit.MINUTES.between(t1, t.getTime());
        return diffMins;
    }
}
