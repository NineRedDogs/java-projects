package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.forecast.BTTS;
import dogs.red.nine.oracle.gatherer.Gatherer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class Oracle {

    private static final Logger logger = LogManager.getLogger("Oracle");

    private final Gatherer dataGatherer;
    private final List<FixtureData> forecastFixtures;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


    public Oracle() throws IOException {
        this.dataGatherer = new Gatherer();
        this.forecastFixtures = new ArrayList<FixtureData>();
    }

    private void generateForecastData() throws ParseException {
        // get todays date
        Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));

        for (FixtureData fixture : dataGatherer.getFixtures()) {

            boolean shouldWeUseThisFixture = (!fixture.getDate().before(todayDate));

            logger.debug("Process ? " + shouldWeUseThisFixture + " - fixture : " + fixture );

            // only check if fixture has not yet happened
            if (AppConstants.DEV_MODE || AppConstants.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES || shouldWeUseThisFixture) {

                fixture.setForecastData(dataGatherer.getFixtureData(fixture));
                forecastFixtures.add(fixture);

//				Team homeTeam = teams.getTeam(fixture.getHomeTeam().getName());
//				Team awayTeam = teams.getTeam(fixture.getAwayTeam().getName());

                // both teams to score
//				f.testBothTeamsToScore(fixture, homeTeam, awayTeam);
            }
        }
    }

    public void forecast() throws ParseException {

        generateForecastData();

        BTTS btts = new BTTS(forecastFixtures);

        String predictionsFor="All fixtures";
        if (AppConstants.DEV_MODE) {
            predictionsFor="sample fixtures (DEV MODE enabled)";
        } else if (AppConstants.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES) {
            predictionsFor="this weeks fixtures (DEV MODE)";
        } else if (AppConstants.ONLY_TODAYS_GAMES) {

            Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));
            dateFormatter.applyPattern("EEEE d MMM yyyy");
            String myDate = dateFormatter.format(todayDate);

            predictionsFor="fixtures played on " + myDate;
        }
        logger.info("\nShowing predictions for " + predictionsFor + "\n\n");
    }

    public static void main(String[] args) throws IOException, ParseException {
        Oracle o = new Oracle();
        o.forecast();
    }


}
