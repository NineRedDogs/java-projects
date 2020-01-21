package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import java.util.*;

public abstract class ForecastType {
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
                FixtureData newEntry = fixData.clone();
                forecastTips.add(newEntry);
            }
        }
        forecastTips.sort(getForecastComparator());
    }

    public void sort(Comparator<FixtureData> sorter) {

    }

    public List<FixtureData> getTips() {
        return forecastTips.subList(0, AppConstants.NUM_TIPS);
    }
}
