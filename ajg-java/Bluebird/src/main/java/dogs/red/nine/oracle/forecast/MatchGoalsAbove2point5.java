package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MatchGoalsAbove2point5 extends MatchGoals2point5 {

    private static final Logger logger = LogManager.getLogger("MatchGoalsAbove2point5");

    public MatchGoalsAbove2point5() {
        super();
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() > AppConstants.HOT_TIP_THRESHOLD_MATCH_ABOVE_2P5);
    }
}
