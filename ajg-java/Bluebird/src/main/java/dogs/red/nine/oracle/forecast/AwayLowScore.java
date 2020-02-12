package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AwayLowScore extends AwayScore {

    private static final Logger logger = LogManager.getLogger("AwayLowScore");

    public AwayLowScore() {
        super();
    }

    @Override
    protected boolean highScoreRequired() {
        return false;
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() < AppConstants.HOT_TIP_THRESHOLD_AWAY_LOW_SCORE);
    }


}
