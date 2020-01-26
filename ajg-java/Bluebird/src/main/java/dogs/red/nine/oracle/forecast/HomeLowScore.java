package dogs.red.nine.oracle.forecast;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HomeLowScore extends HomeScore {

    private static final Logger logger = LogManager.getLogger("HomeLowScore");

    public HomeLowScore() {
        super();
    }

    @Override
    protected boolean highScoreRequired() {
        return false;
    }
}
