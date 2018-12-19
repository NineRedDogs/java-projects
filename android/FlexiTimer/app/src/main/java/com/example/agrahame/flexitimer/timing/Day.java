package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;

import java.time.LocalDate;

public abstract class Day {

    private TimePair inOut;

    public void clockIn(Time clockInTime) {
        inOut.setT1(clockInTime);
    }

    public void clockOut(Time clockOutTime) {
        inOut.setT2(clockOutTime);
    }

    public LocalDate getDate() throws TimeException {
        if (inOut.getT1() == null) {
            throw new TimeException("No times entered yet, so cannot determine date");
        }
        return inOut.getT1().getTime().toLocalDate();

    }

    public abstract void


}
