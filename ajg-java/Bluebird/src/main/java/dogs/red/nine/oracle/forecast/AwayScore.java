package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AwayScore extends TeamScore {

    private static final Logger logger = LogManager.getLogger("AwayScore");

    public AwayScore() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        return getHighScoreRating(at, ht);
    }
}
