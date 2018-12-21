package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class DayWithTimes extends Day {

    protected final FlexTimePair inOut;
    protected final String abc="123";

    public void clockIn(FlexTime clockInTime) {
        inOut.setT1(clockInTime);
    }
    public void clockOut(FlexTime clockOutTime) {
        inOut.setT2(clockOutTime);
    }

    public DayWithTimes(String name, String date, FlexTimePair inOut) {
        super(name, date);
        this.inOut = inOut;
    }

    public FlexTimePair getInOut() {
        return inOut;
    }

}
