package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.gatherer.Gatherer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class Oracle {

    private static final Logger logger = LogManager.getLogger("Oracle");

    public static void main(String[] args) throws IOException, ParseException {
        Gatherer g = new Gatherer();



        // get todays date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));

        for (FixtureData fixture : g.getFixtures()) {

            boolean processFixture = (!fixture.getDate().before(todayDate));

            logger.debug("Process ? " + processFixture + " - fixture : " + fixture );

            // only check if fixture has not yet happened
            if (DEV_MODE || DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES || processFixture) {

                TeamData homeTeam =

//				Team homeTeam = teams.getTeam(fixture.getHomeTeam().getName());
//				Team awayTeam = teams.getTeam(fixture.getAwayTeam().getName());

                // both teams to score
//				f.testBothTeamsToScore(fixture, homeTeam, awayTeam);
            }
        }

        String predictionsFor="All fixtures";
        if (DEV_MODE) {
            predictionsFor="sample fixtures (DEV MODE enabled)";
        } else if (DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES) {
            predictionsFor="this weeks fixtures (DEV MODE)";
        } else if (ONLY_TODAYS_GAMES) {

            dateFormatter.applyPattern("EEEE d MMM yyyy");
            String myDate = dateFormatter.format(todayDate);

            predictionsFor="fixtures played on " + myDate;
        }
        logger.info("\nShowing predictions for " + predictionsFor + "\n\n");
    }


}
