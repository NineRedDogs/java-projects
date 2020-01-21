package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.TeamForecastData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class BTTS extends ForecastType {

    private static final Logger logger = LogManager.getLogger("BTTS");

    public BTTS() {
        super();
    }

    @Override
    protected float calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        if (ht.)

    }

    @Override
    protected float getForecastThreshold() {
        return 50.0f;
    }

    final int MAGIC_THRESHOLD_AWAY_TO_CONCEDE = 66;



    /*public void display(int numFixturesToDisplay) {

        int count=1;
        logger.info("---------------------------------------");
        logger.info("---   Both teams to score");
        logger.info("-------------------------");
        for (FixtureData fixture : fixtures) {

            FixtureBothScore fbs = (FixtureBothScore) fixture;
            Team homeTeam = fixture.getHomeTeam();
            Team awayTeam = fixture.getAwayTeam();

            logger.info(fixture.fixturePrint() + "  [** " + (fbs.getHomeTeamToScoreRating()+fbs.getAwayTeamToScoreRating()) + " **] --- (H:"
                    + fbs.getHomeTeamToScoreRating() + " A:" + fbs.getAwayTeamToScoreRating() + ")");
            if (Forecaster.SHOW_DETAILED_STATS) {
                logger.info("     " + homeTeam.getHomeStats());
                logger.info("     " + awayTeam.getAwayStats() + "\n");
            }

            if (count++ >= numFixturesToDisplay) break;
        }
        logger.info("---------------------------------------");


    }*/
}
