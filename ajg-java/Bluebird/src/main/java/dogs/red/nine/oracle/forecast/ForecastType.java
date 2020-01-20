package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.gatherer.Gatherer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public abstract class ForecastType {
    Set<FixtureData> fixtures = new TreeSet<FixtureData>();

    public abstract void process(FixtureData fd);

    public boolean addPrediction(Date fixtureDate) {
        if ((Gatherer.DEV_MODE) || ((Gatherer.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES) )) {
            return true;
        } else if (Gatherer.ONLY_TODAYS_GAMES) {

            // get todays date
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));
                return fixtureDate.equals(todayDate);
            } catch (ParseException e) {
            }
            return true;
        } else {
            return true;
        }
    }

    public Set<FixtureData> getFixtures() {
        return fixtures;
    }
}
