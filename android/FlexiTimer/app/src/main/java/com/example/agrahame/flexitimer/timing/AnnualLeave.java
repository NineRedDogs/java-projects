package com.example.agrahame.flexitimer.timing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnnualLeave extends Day {

    /* json discriminator */
    public static final String CLASS_NAME = "AnnualLeave";

    @JsonCreator
    public AnnualLeave (@JsonProperty("date") String date) {
        super(CLASS_NAME, date);
        //System.out.println("Creating Annual Leave [" + date + "]");
    }
}
