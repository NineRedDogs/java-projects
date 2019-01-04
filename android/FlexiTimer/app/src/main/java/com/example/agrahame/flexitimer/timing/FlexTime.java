package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FlexTime {

    public final static String TIME_FORMAT = "HH:mm";
    private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    final private String t1;

    public FlexTime() {
        t1 = LocalDateTime.now().toString();
    }

    @JsonCreator
    public FlexTime(@JsonProperty("time") String time) {
        this.t1 = time;
    }

    public String getTime() {
        return t1;
    }

    public LocalTime asLocalTime() {
        return LocalTime.parse(t1);
    }

    public long diffMins(FlexTime t) throws TimeException {
        if (t1 == null || t == null) {
            throw new TimeException("need 2 times to calculate difference");
        }
        //long diffMins = ChronoUnit.MINUTES.between(asLocalTime(), t.asLocalTime());
        Duration d = Duration.between(t.asLocalTime(), asLocalTime());
        return d.toMinutes();
    }

    public static LocalTime parseTime(final String t) {
        return LocalTime.parse(t, TIME_FORMATTER);
    }

}
