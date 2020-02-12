package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HomeWin extends TeamToWin {

    private static final Logger logger = LogManager.getLogger("HomeWin");

    public HomeWin() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        return getToWinScore(ht, at);
    }

    @Override
    protected boolean isHotTip(FixtureData tip) {
        return (tip.getForecastData().getForecastScore() > AppConstants.HOT_TIP_THRESHOLD_HOME_WIN);
    }

}
