package com.example.agrahame.flexitimer.timing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UnknownDayType extends Day {

    private String temp;

    /* json discriminator */
    public static final String CLASS_NAME = "UnknownDayType";


    @JsonCreator
    public UnknownDayType(@JsonProperty("date") String date) {
        super(CLASS_NAME, date);
        this.temp = "xyz";
        //System.out.println("Creating Unknown day type [" + date + "]");
    }
}
