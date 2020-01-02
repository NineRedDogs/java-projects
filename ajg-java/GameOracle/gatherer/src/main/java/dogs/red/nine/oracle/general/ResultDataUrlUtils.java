package dogs.red.nine.oracle.general;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.gatherer.Gatherer;

public class ResultDataUrlUtils {

    private static final String RESULTS_WEBSITE = "http://www.football-data.co.uk/";
    private static final String RESULTS_AREA = RESULTS_WEBSITE + "mmz4281/" + Gatherer.SEASON_TO_USE + "/";
    
    public static String generateResultUrl(Division division) {

        StringBuilder sb = new StringBuilder(RESULTS_AREA);

        final String divisionFileName = division.getDivCode() + ".csv";

        sb.append(divisionFileName);

        return sb.toString();
    }

}