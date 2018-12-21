package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonSubTypes({
        @JsonSubTypes.Type(value = StandardDay.class, name = StandardDay.CLASS_NAME),
        @JsonSubTypes.Type(value = HalfDay.class, name = HalfDay.CLASS_NAME),
        @JsonSubTypes.Type(value = UnknownDayType.class, name = UnknownDayType.CLASS_NAME),
        @JsonSubTypes.Type(value = AnnualLeave.class, name = AnnualLeave.CLASS_NAME) })
public abstract class Day implements Serializable {

    /* discriminator/determinator */
    private final String name;

    public final static String DATE_FORMAT = "dd/MM/yyyy";
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    protected final String date;

    public Day(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public LocalDate toLocalDate() {
        return parseDate(date);
    }


    /**
     * Utility methods .....
     */

    public static LocalDate parseDate(final String d) {
        return LocalDate.parse(d, DATE_FORMATTER);
    }

}
