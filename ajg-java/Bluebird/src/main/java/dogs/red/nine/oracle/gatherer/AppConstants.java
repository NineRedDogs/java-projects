package dogs.red.nine.oracle.gatherer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConstants {

    private static final Logger logger = LogManager.getLogger("AppConstants");

    public static final int CURRENT_FORM_GAMES = 4;

    private int doDiv(int a, int b) {
        float x = (float) ((double)a / b);
        int r = Math.round(x);
        logger.debug("div: " + a + " / " + b + " = " + r + "(" + x + ")");
        return r;
    }
    public static void main(String[] args) {

        AppConstants ac = new AppConstants();
        ac.doDiv(15,4);
        ac.doDiv(14,4);
        ac.doDiv(13,4);
        ac.doDiv(12, 4);
    }
}
