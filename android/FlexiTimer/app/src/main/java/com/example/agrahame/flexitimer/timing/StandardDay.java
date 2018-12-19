package com.example.agrahame.flexitimer.timing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StandardDay extends Day {
    private TimePair lunch;

    public StandardDay() {
        super();
    }

    public StandardDay(String d, String t1, String t2, String t3, String t4) {
        super();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        String date = "16/08/2016";

        LocalDate localDate = LocalDate.parse(date, formatter);
    }

    public void clockOutLunch(Time clockOutLunchTime) {
        lunch.setT1(clockOutLunchTime);
    }

    public void clockInLunch(Time clockInLunchTime) {
        lunch.setT2(clockInLunchTime);
    }


}
