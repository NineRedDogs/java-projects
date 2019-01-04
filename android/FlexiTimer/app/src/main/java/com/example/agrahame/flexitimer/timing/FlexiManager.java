package com.example.agrahame.flexitimer.timing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlexiManager implements Serializable {

    private List<Day> days;

    public List<Day> getDays() {
        return days;
    }

    private static final File DATA_DIR =
            new File (System.getProperty("user.dir") + File.separator+ "app" + File.separator + "data" + File.separator);
    private static final String TIMES_CSV_FILE = "times.csv";
    private static final String TIMES_JSON_FILE = "times.json";
    public static final double WORKING_WEEK_NUM_HOURS = 39.5;
    public static final double WORKING_WEEK_NUM_DAYS = 5;

    /**
     * Using a source CSV file in the following format ;
     *
     * Date,Flex,FlexTime IN,Lunch OUT,Lunch IN,FlexTime OUT,Lunch Duration,Total (Day),Day (+/-),Notes,39:30:00,07:54
     * 23/08/2018,#NAME?,09:00,12:00,12:45,16:30,0:45,06:45,#NAME?,,,
     * 24/08/2018,#NAME?,07:30,12:00,12:45,16:15,0:45,08:00,#NAME?,,,
     * 27/08/2018,#NAME?,,,,,,,,Bank Holiday,,
     *
     *
     * @throws IOException
     */
    public void readInCsv() throws IOException {
        if (DATA_DIR.exists()) {

            final File csvFile = new File(DATA_DIR, TIMES_CSV_FILE);

            if (csvFile.exists()) {

                Pattern pattern = Pattern.compile(",");

                try (BufferedReader in = new BufferedReader(new FileReader(csvFile));) {
                    days = in.lines().skip(1).map(line -> {

                        final int dateColumn = 0;
                        final int inDayColumn = 2;
                        final int outLunchColumn = 3;
                        final int inLunchColumn = 4;
                        final int outDayColumn = 5;
                        String[] x = pattern.split(line);
                        System.out.println("parsing line [" + line + "] split [" + x.length + "]");
                        final String date = x[dateColumn];
                        if (x.length > outDayColumn) {
                            final String inDay = x[inDayColumn];
                            final String outLunch = x[outLunchColumn];
                            final String inLunch = x[inLunchColumn];
                            final String outDay = x[outDayColumn];

                            if (!timeExists(inDay)) {
                                // no times - assume absence
                                return new AnnualLeave(date);
                            } else if (!timeExists(outLunch)) {
                                // no lunch - assume half day
                                return new HalfDay(date, inDay, outDay);
                            } else if (timeExists(inDay) && timeExists(outDay) && timeExists(inLunch) && timeExists(outLunch)) {
                                // got 4 times, create a standard day
                                return new StandardDay(date, inDay, outLunch, inLunch, outDay);
                            }
                        }
                        return new UnknownDayType(date);
                    }).collect(Collectors.toList());
                }
            } else {
                System.out.println("Could not find csv file [" + csvFile.getAbsolutePath() + "]");
            }
        } else {
            System.out.println("Could not find data dir [" + DATA_DIR.getAbsolutePath() + "]");
        }
    }

    public void readInJson() throws IOException {

        if (DATA_DIR.exists()) {

            final File jsonFile = new File(DATA_DIR, TIMES_JSON_FILE);

            if (jsonFile.exists()) {
                byte[] jsonData = Files.readAllBytes(jsonFile.toPath());

                ObjectMapper om = new ObjectMapper();

                Days days = om.readValue(jsonData, Days.class);

                days.sortByDate();
            }
        }
    }

    private LocalTime getTime(String t) {
        LocalTime lt = null;
        try {
            lt = FlexTime.parseTime(t);
        } catch (DateTimeParseException dtpe) {
            //System.out.println("Error parsing time [" + t + "]");
        }
        return lt;
    }

    private boolean timeExists(String t) {
        if (t != null && !t.isEmpty()) {
            // there is a value, parse it - to see if its the expected time format, i.e. hh:mm
            try {
                FlexTime.parseTime(t);
                return true;
            } catch (DateTimeParseException dtpe) {
                // do nothing, just return false
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        final boolean csvIsSource = false;
        //final boolean csvIsSource = true;
        FlexiManager fm = new FlexiManager();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        if (csvIsSource) {
            fm.readInCsv();

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            File jsonTimesFile = new File(DATA_DIR, TIMES_JSON_FILE);
            mapper.writeValue(jsonTimesFile, fm);
            mapper.writeValue(System.out, fm);
        } else {
            System.out.println("reading in json file ...@);");

            fm.readInJson();
        }


    }

}
