package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HalfDay extends DayWithTimes {

    /* json discriminator */
    public static final String CLASS_NAME = "HalfDay";

    /** expected number of minutes to work in a halfday */

    //public static final int HALF_DAY_MINS = StandardDay.STANDARD_DAY_MINS / 2;
    public static final int HALF_DAY_MINS = StandardDay.STANDARD_DAY_MINS;


    @JsonCreator
    public HalfDay(@JsonProperty("date") String date,
                   @JsonProperty("inDay") String inDay,
                   @JsonProperty("outDay") String outDay) {
        super(CLASS_NAME, date, new FlexTimePair(inDay, outDay));
        /*System.out.println("Creating half day : d [" + date +
                "] in [" + inDay +
                "] out [" + outDay + "]");*/
    }

    @Override
    public long getMinutesWorked() throws TimeException {
        return inOut.calcDiffMins();
    }

    @Override
    public long getExpectedMinutesWorked() throws TimeException {
        return HALF_DAY_MINS;
    }



}
