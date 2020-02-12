package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AwayHighScore extends AwayScore {

    private static final Logger logger = LogManager.getLogger("AwayHighScore");

    public AwayHighScore() {
        super();
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() > AppConstants.HOT_TIP_THRESHOLD_AWAY_HIGH_SCORE);
    }


}
