package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class ForecastType {
    private static final Logger logger = LogManager.getLogger("ForecastType");

    private final List<FixtureData> forecastTips = new ArrayList<FixtureData>();

    public ForecastType() {}

    protected abstract float calcForecastScore(FixtureData fd);
    protected abstract float getForecastThreshold();

    public void process(final List<FixtureData> forecastFixtures) {

        // loop around all fixtures
        // calculate a forecast score - save to ForecastData.forecastScore()
        // if score is above threshold then
        // add to sorted list (sort by BTTS score, i.e. ForecastData.forecastScore())

        for (FixtureData fixData : forecastFixtures) {
            float score = calcForecastScore(fixData);

            if (score > getForecastThreshold()) {
                try {
                    FixtureData newEntry = (FixtureData) fixData.clone();
                    forecastTips.add(newEntry);

                } catch (CloneNotSupportedException e) {
                    logger.debug("Failed to clone ForecastType object, e:" + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
        //forecastTips.sort(getForecastComparator());
        forecastTips.sort(getForecastComparator());
    }

    // override if different method of sorting required in the sub-class
    protected Comparator<? super FixtureData> getForecastComparator() {
        return new SortByForecastScore();
    }


    public List<FixtureData> getTips() {
        return forecastTips.subList(0, AppConstants.NUM_TIPS);
    }

    class SortByForecastScore implements Comparator<FixtureData>
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
