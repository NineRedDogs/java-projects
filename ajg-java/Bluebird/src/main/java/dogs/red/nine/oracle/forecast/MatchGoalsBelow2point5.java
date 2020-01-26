package dogs.red.nine.oracle.forecast;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MatchGoalsBelow2point5 extends MatchGoals2point5 {

    private static final Logger logger = LogManager.getLogger("MatchGoalsBelow2point5");

    public MatchGoalsBelow2point5() {
        super();
    }

    @Override
    protected boolean highScoreRequired() {
        return false;
    }


}
