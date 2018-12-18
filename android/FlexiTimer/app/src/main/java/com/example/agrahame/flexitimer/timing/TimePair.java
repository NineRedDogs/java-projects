package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;

import java.time.LocalDateTime;

public class TimePair {
    Time t1;
    Time t2;

    public TimePair(Time t1, Time t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public long getDiffMins() throws TimeException {
        return t2.diffMins(t1);
    }


}
