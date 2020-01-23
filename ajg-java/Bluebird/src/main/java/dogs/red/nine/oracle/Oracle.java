package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import dogs.red.nine.oracle.forecast.BTTS;
import dogs.red.nine.oracle.forecast.Forecaster;
import dogs.red.nine.oracle.gatherer.Gatherer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Oracle {

    private static final Logger logger = LogManager.getLogger("Oracle");

    /** responsible for gathering data, e.g. getting latest results data and also grabbing the fixtures */
    private final Gatherer dataGatherer;

    /** responsible for running the forecasting algorithms and identifying the tips. */
    private final Forecaster forecaster;

    /** subset of the full set of current fixtures, containing the ones 'chosen' by the predictor algorithms */
    private final List<FixtureData> forecastFixtures;


    public Oracle() throws IOException {
        dataGatherer = new Gatherer();
        forecaster = new Forecaster(dataGatherer.getTableGenerator());
        forecastFixtures = new ArrayList<FixtureData>();
    }

    public void forecast() {
        forecaster.forecast(dataGatherer.getFixtures());
    }


    public static void main(String[] args) throws IOException, ParseException {
        Oracle o = new Oracle();
        o.forecast();
    }


}
