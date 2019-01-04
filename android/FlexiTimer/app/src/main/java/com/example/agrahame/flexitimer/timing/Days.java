package com.example.agrahame.flexitimer.timing;

import com.example.agrahame.flexitimer.timing.exceptions.TimeException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Days implements Serializable {

    private List<Day> days;

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public void sortByDate() {
        days.sort(new Comparator<Day>() {
            @Override
            public int compare(Day d1, Day d2) {
                if (d1.toLocalDate().isAfter(d2.toLocalDate())) {
                    return 1;
                }
                return -1;
            }
        });

        //days.forEach((k)->System.out.println(k.getDate()));

        long flex = days.stream().mapToLong(l -> {
            try {
                return l.getFlex();
            } catch (TimeException te) {
                System.out.println("Error processing day : " + l + ":" + te.getMessage());
                return 0;
            }
        }).sum();

        System.out.println("Flex : " + flex + " mins");
        System.out.println("Flex : " + flex/60 + " hours");
    }
}
