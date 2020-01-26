package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AwayWin extends TeamToWin {

    private static final Logger logger = LogManager.getLogger("AwayWin");

    public AwayWin() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        return getToWinScore(at, ht);
    }



}
