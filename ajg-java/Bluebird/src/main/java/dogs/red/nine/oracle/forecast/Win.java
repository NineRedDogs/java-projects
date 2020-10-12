package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.forecast.comparator.SortByHighForecastScore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Win {

    private static final Logger logger = LogManager.getLogger("HomeWin");


    private final List<FixtureData> allTips;

    public Win() {
        allTips = new ArrayList<>();
    }


    private List<FixtureData> getTips() {
        final int maxTips = (allTips.size() > AppConstants.NUM_ALL_WIN_TIPS) ? AppConstants.NUM_ALL_WIN_TIPS : allTips.size();
        return allTips.subList(0, maxTips);
    }

    public void displayTips(String desc) {
        logger.debug(desc);
        for (FixtureData tip : getTips()) {
            logger.debug("   " + tip.asString(true));
        }
    }

    public void addHomeWinTips(HomeWin hw) {
        processTips(hw.getTips(), true);
    }

    public void addAwayWinTips(AwayWin aw) {
        processTips(aw.getTips(), false);
    }

    private void processTips(List<FixtureData> tips, boolean homeTips) {
        for (FixtureData tip : tips) {
            if (tip.getForecastData().getForecastScore() > AppConstants.HOT_TIP_THRESHOLD_ANY_WIN) {
                // good enough tip, so now deep copy to avoid forecast data being overwritten by subsequent forecast calls
                FixtureData f = new FixtureData(tip);
                if (homeTips) {
                    f.highlightHomeTeam();
                } else {
                    f.highlightAwayTeam();
                }
                allTips.add(f);
            }
        }
        allTips.sort(new SortByHighForecastScore());
    }

}
