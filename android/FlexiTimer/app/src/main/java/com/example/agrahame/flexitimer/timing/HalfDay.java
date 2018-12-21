package com.example.agrahame.flexitimer.timing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HalfDay extends DayWithTimes {

    /* json discriminator */
    public static final String CLASS_NAME = "HalfDay";


    public HalfDay(String date, String inDay, String outDay) {
        super(CLASS_NAME, date, new FlexTimePair(inDay, outDay));
        System.out.println("Creating half day : d [" + date +
                "] in [" + inDay +
                "] out [" + outDay + "]");
    }


}
