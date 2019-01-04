package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FlexTimePair {
    private FlexTime t1;
    private FlexTime t2;

    public FlexTimePair() {
    }

    @JsonCreator
    public FlexTimePair(@JsonProperty("t1") FlexTime t1,
                        @JsonProperty("t2") FlexTime t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public FlexTimePair(String t1, String t2) {
        this.t1 = new FlexTime(t1);
        this.t2 = new FlexTime(t2);
    }

    public FlexTime getT1() {
        return t1;
    }

    public void setT1(FlexTime t1) {
        this.t1 = t1;
    }

    public FlexTime getT2() {
        return t2;
    }

    public void setT2(FlexTime t2) {
        this.t2 = t2;
    }

    public long calcDiffMins() throws TimeException {
        return t2.diffMins(t1);
    }


}
