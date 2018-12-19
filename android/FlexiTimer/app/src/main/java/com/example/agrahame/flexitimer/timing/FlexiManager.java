package com.example.agrahame.flexitimer.timing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlexiManager {

    private List<Day> flexiTimes;


    /**
     * Using a source CSV file in the following format ;
     *
     * Date,Flex,Time IN,Lunch OUT,Lunch IN,Time OUT,Lunch Duration,Total (Day),Day (+/-),Notes,39:30:00,07:54
     * 23/08/2018,#NAME?,09:00,12:00,12:45,16:30,0:45,06:45,#NAME?,,,
     * 24/08/2018,#NAME?,07:30,12:00,12:45,16:15,0:45,08:00,#NAME?,,,
     * 27/08/2018,#NAME?,,,,,,,,Bank Holiday,,
     *
     *
     * @throws IOException
     */
    public void readInCsv() throws IOException {
        final String csvFile = "times.csv";

        Pattern pattern = Pattern.compile(",");

        try (BufferedReader in = new BufferedReader(new FileReader(csvFile));) {
            List<StandardDay> times = in.lines().skip(1).map(line -> {
                String[] x = pattern.split(line);
                String d = x[0];
                String t1 = x[2];
                String t2 = x[3];
                String t3 = x[4];
                String t4 = x[5];

                return new StandardDay(d, t1, t2,t3, t4);
            }).collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(System.out, times);
        }
    }

}
