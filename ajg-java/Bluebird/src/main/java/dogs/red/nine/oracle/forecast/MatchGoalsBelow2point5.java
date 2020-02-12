package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MatchGoalsBelow2point5 extends MatchGoals2point5 {

    private static final Logger logger = LogManager.getLogger("MatchGoalsBelow2point5");

    public MatchGoalsBelow2point5() {
        super();
    }

    @Override
    protected boolean highScoreRequired() {
        return false;
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() < AppConstants.HOT_TIP_THRESHOLD_MATCH_BELOW_2P5);
    }
}
