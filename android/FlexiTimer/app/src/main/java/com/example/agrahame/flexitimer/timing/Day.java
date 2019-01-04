package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StandardDay.class, name = StandardDay.CLASS_NAME),
        @JsonSubTypes.Type(value = HalfDay.class, name = HalfDay.CLASS_NAME),
        @JsonSubTypes.Type(value = UnknownDayType.class, name = UnknownDayType.CLASS_NAME),
        @JsonSubTypes.Type(value = AnnualLeave.class, name = AnnualLeave.CLASS_NAME) })
public abstract class Day implements Serializable {

    /* discriminator/determinator */
    @JsonProperty
    private final String name;

    public final static String DATE_FORMAT = "dd/MM/yyyy";
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @JsonProperty
    protected final String date;

    @JsonCreator
    public Day(@JsonProperty("name") String name,
               @JsonProperty("date") String date) {
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

    public long getMinutesWorked() throws TimeException {
        return 0;
    }

    public long getExpectedMinutesWorked() throws TimeException {
        return 0;
    }

    public long getFlex() throws TimeException {
        System.out.println("Date : " + date + "[" + (getMinutesWorked() - getExpectedMinutesWorked()) + "] >> mins (worked) : " + getMinutesWorked() + " mins (exp) : " + getExpectedMinutesWorked() );
        return getMinutesWorked() - getExpectedMinutesWorked();
    }

    /**
     * Utility methods .....
     */

    public static LocalDate parseDate(final String d) {
        return LocalDate.parse(d, DATE_FORMATTER);
    }

}
