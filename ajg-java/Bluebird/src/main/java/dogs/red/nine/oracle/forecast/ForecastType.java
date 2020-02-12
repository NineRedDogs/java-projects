package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class ForecastType {
    private static final Logger logger = LogManager.getLogger("ForecastType");

    protected static final float FORM_GENERAL_MULTIPLIER = 0.80f;
    protected static final float SEASON_VENUE_MULTIPLIER = 0.75f;

    private final List<FixtureData> forecastTips = new ArrayList<FixtureData>();

    public ForecastType() {}

    protected abstract int calcForecastScore(FixtureData fd);

    protected boolean highScoreRequired() {
        // default mode for our algorithms is look for the highest - override this to return false in algorithms where
        // we want to get low scores
        return true;
    }

    public void process(final List<FixtureData> forecastFixtures) {

        // loop around all fixtures
        // calculate a forecast score - save to ForecastData.forecastScore()
        // if score is above threshold then
        // add to sorted list (sort by BTTS score, i.e. ForecastData.forecastScore())

        for (FixtureData fixData : forecastFixtures) {
            float score = calcForecastScore(fixData);

            //if (score > getForecastThreshold()) {
                try {
                    FixtureData newEntry = (FixtureData) fixData.clone();
                    newEntry.getForecastData().setForecastScore(score);
                    //logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    //logger.debug("score : " + score);
                    //logger.debug("Orig  : " + fixData);
                    //logger.debug("Clone : " + newEntry);
                    //logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    forecastTips.add(newEntry);
                } catch (CloneNotSupportedException e) {
                    logger.debug("Failed to clone ForecastType object, e:" + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            //}
        }
        if (highScoreRequired()) {
            forecastTips.sort(getForecastComparatorHigh());
        } else {
            forecastTips.sort(getForecastComparatorLow());
        }
    }

    // override if different method of sorting required in the sub-class
    protected Comparator<? super FixtureData> getForecastComparatorHigh() {
        return new SortByHighForecastScore();
    }

    // override if different method of sorting required in the sub-class
    protected Comparator<? super FixtureData> getForecastComparatorLow() {
        return new SortByLowForecastScore();
    }


    public List<FixtureData> getTips() {
        final int maxTips = (forecastTips.size() > AppConstants.NUM_TIPS) ? AppConstants.NUM_TIPS : forecastTips.size();
        return forecastTips.subList(0, maxTips);
    }

    public void displayTips(final String desc) {
        logger.debug(desc);
        for (FixtureData tip : getTips()) {
            if (isHotTip(tip)) {
                logger.debug("   " + tip.asString(true));
            }
        }
    }

    //protected abstract float getHotTipThreshold();

    protected abstract boolean isHotTip(FixtureData tip);


    class SortByHighForecastScore implements Comparator<FixtureData>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(FixtureData a, FixtureData b)
        {
            if (a.getForecastData().getForecastScore() < b.getForecastData().getForecastScore()) {
                return 1;
            } else if (a.getForecastData().getForecastScore() == b.getForecastData().getForecastScore()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    class SortByLowForecastScore implements Comparator<FixtureData>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(FixtureData a, FixtureData b)
        {
            if (a.getForecastData().getForecastScore() > b.getForecastData().getForecastScore()) {
                return 1;
            } else if (a.getForecastData().getForecastScore() == b.getForecastData().getForecastScore()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
