package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HomeHighScore extends HomeScore {

    private static final Logger logger = LogManager.getLogger("HomeHighScore");

    public HomeHighScore() {
        super();
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() > AppConstants.HOT_TIP_THRESHOLD_HOME_HIGH_SCORE);
    }


}
