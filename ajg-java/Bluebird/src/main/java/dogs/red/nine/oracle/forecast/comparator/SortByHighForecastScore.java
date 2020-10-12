package dogs.red.nine.oracle.forecast.comparator;

import dogs.red.nine.oracle.data.FixtureData;

import java.util.Comparator;

public class SortByHighForecastScore implements Comparator<FixtureData>
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