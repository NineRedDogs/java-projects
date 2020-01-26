package dogs.red.nine.oracle.forecast;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AwayLowScore extends AwayScore {

    private static final Logger logger = LogManager.getLogger("AwayLowScore");

    public AwayLowScore() {
        super();
    }

    @Override
    protected boolean highScoreRequired() {
        return false;
    }
}
