package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.Division;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AppConstants {

    private static final String DATA_DIR_NAME = "data";

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File DATA_DIR = new File(CWD, DATA_DIR_NAME);


    // set this to use a local sample fixture file, instead of trying to retrieve from remote site
    public static final boolean DEV_MODE = true;
    public static final File SAMPLE_FIXTURE_DEV_MODE_FILE=new File(DATA_DIR, "sample_fixtures_used_for_dev_mode.csv");

    // use this flag to use this weeks fixtures that have already been played, useful if hanging around for Friday in order to get some useful runs....
    // this flag means we'll go off and get the fixture data from remote website - instead of using the local hard-coded fixture file used when DEV_MODE flag is set
    // note: if this is true, then set DEV_MODE to false, otherwise local fixtures sample file will be used ...
    public static final boolean DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES = false;


    /** Set this to only list predictions for todays games */
    public static final boolean ONLY_TODAYS_GAMES = true;
    //public static final boolean ONLY_TODAYS_GAMES = false;

    /** Set to true if use UK divisions, false to use EURO leagues */
    public static final boolean USE_UK_LEAGUES = true;
    //public static final boolean USE_UK_LEAGUES = false;

    private static final List<Division> EPL = Arrays.asList(
            Division.England_Premier_League);

    private static final List<Division> ENG_TOP2 = Arrays.asList(
            Division.England_Premier_League,
            Division.England_Championship);

    private static final List<Division> ENG_DIVISIONS = Arrays.asList(
            Division.England_Premier_League,
            Division.England_Championship,
            Division.England_League_1,
            Division.England_League_2,
            Division.England_Conference);

    private static final List<Division> UK_DIVISIONS = Arrays.asList(
            Division.England_Premier_League,
            Division.England_Championship,
            Division.England_League_1,
            Division.England_League_2,
            Division.England_Conference,
            Division.Scotland_Premier_League ,
            Division.Scotland_Championship ,
            Division.Scotland_Div_1 ,
            Division.Scotland_Div_2);

    private static final List<Division> EURO_DIVISIONS = Arrays.asList(
            Division.Germany_Bundesliga_1 ,
            Division.Spain_La_Liga ,
            Division.Italy_Serie_A ,
            Division.France_Ligue_1 ,
            Division.Holland_Eridivisie ,
            Division.Belgium_Juliper_1 ,
            Division.Portugal_Primera );

    public static final boolean SHOW_DETAILED_STATS = false;

    public static final String SEASON_TO_USE = System.getProperty("oracle.season", "1920");


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
