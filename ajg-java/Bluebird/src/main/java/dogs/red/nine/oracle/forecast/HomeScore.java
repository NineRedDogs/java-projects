package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class HomeScore extends TeamScore {

    private static final Logger logger = LogManager.getLogger("HomeScore");

    public HomeScore() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        return getHighScoreRating(ht, at);
    }



}
