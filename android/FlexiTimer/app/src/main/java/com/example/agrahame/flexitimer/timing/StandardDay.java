package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StandardDay extends DayWithTimes {

    /* json discriminator */
    public static final String CLASS_NAME = "StandardDay";

    /** expected number of minutes to work in a standard day */
    private static final double STANDARD_DAY_HOURS = FlexiManager.WORKING_WEEK_NUM_HOURS / FlexiManager.WORKING_WEEK_NUM_DAYS;
    public static final int STANDARD_DAY_MINS = (int) Math.floor(STANDARD_DAY_HOURS * 60);


    protected FlexTimePair lunch;

    public FlexTimePair getLunch() {
        return lunch;
    }

    public void setLunch(FlexTimePair lunch) {
        this.lunch = lunch;
    }

    public StandardDay(String date,
                       String inDay,
                       String outLunch,
                       String inLunch,
                       String outDay) {
        super(CLASS_NAME, date, new FlexTimePair(inDay, outDay));

        lunch = new FlexTimePair(outLunch, inLunch);

        System.out.println("Creating standard day : d [" + date +
                "] in [" + inDay +
                "] out (lunch) [" + outLunch +
                "] in (lunch) [" + inLunch +
                "] out [" + outDay + "]");
    }

    @JsonCreator
    public StandardDay(@JsonProperty("date") String date,
                       @JsonProperty("inOut") FlexTimePair inOut,
                       @JsonProperty("lunch") FlexTimePair lunch) {
        super(CLASS_NAME, date, inOut);

        this.lunch = lunch;

        /*System.out.println("Creating standard day : d [" + date +
                "] in [" + inOut.getT1().getTime() +
                "] out (lunch) [" + lunch.getT1().getTime() +
                "] in (lunch) [" + lunch.getT2().getTime() +
                "] out [" + inOut.getT1().getTime() + "]");*/
    }

    public StandardDay(String date, String inDay, String outDay) {
        super(CLASS_NAME, date, new FlexTimePair(inDay, outDay));

        System.out.println("Creating standard day : d [" + date +
                "] in [" + inDay +
                "] out (lunch) [TBD] in (lunch) [TBD] out [" + outDay + "]");
    }

    public void clockOutLunch(FlexTime clockOutLunchTime) {
        lunch.setT1(clockOutLunchTime);
    }

    public void clockInLunch(FlexTime clockInLunchTime) {
        lunch.setT2(clockInLunchTime);
    }

    @Override
    public long getMinutesWorked() throws TimeException {
        return inOut.calcDiffMins() - lunch.calcDiffMins();
    }

    @Override
    public long getExpectedMinutesWorked() throws TimeException {
        return STANDARD_DAY_MINS;
    }





}
