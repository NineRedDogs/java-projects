package dogs.red.nine.oracle.general;

import dogs.red.nine.oracle.data.Division;

public class ResultDataUrlUtils {

    private static final String RESULTS_WEBSITE = "http://www.football-data.co.uk/";
    private static final String RESULTS_AREA = RESULTS_WEBSITE + "mmz4281/";

    public static String generateResultUrl(Division division, String seasonToUse) {

        StringBuilder sb = new StringBuilder(RESULTS_AREA);

        sb.append(seasonToUse + "/");

        final String divisionFileName = division.getDivCode() + ".csv";

        sb.append(divisionFileName);

        return sb.toString();
    }

}