package com.example.agrahame.flexitimer.timing;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnnualLeave extends Day {

    /* json discriminator */
    public static final String CLASS_NAME = "AnnualLeave";

    public AnnualLeave (String date) {
        super(CLASS_NAME, date);
        System.out.println("Creating Annual Leave [" + date + "]");
    }
}
