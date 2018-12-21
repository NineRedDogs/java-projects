package com.example.agrahame.flexitimer.timing;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StandardDay extends DayWithTimes {

    /* json discriminator */
    public static final String CLASS_NAME = "StandardDay";

    protected FlexTimePair lunch;

    public FlexTimePair getLunch() {
        return lunch;
    }

    public void setLunch(FlexTimePair lunch) {
        this.lunch = lunch;
    }

    public StandardDay(String date, String inDay, String outLunch, String inLunch, String outDay) {
        super(CLASS_NAME, date, new FlexTimePair(inDay, outDay));

        lunch = new FlexTimePair(outLunch, inLunch);

        System.out.println("Creating standard day : d [" + date +
                "] in [" + inDay +
                "] out (lunch) [" + outLunch +
                "] in (lunch) [" + inLunch +
                "] out [" + outDay + "]");
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

}
