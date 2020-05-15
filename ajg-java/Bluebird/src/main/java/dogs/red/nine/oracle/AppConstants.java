package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.Division;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppConstants {

    public static final int NUM_TIPS = 10;
    public static final boolean JUST_USE_VENUE_FORM = false;
    public static final float HOT_TIP_THRESHOLD_MATCH_BELOW_2P5 = 500.0f;
    public static final float HOT_TIP_THRESHOLD_MATCH_ABOVE_2P5 = 100.0f;
    public static final float HOT_TIP_THRESHOLD_HOME_WIN = 800.0f;
    public static final float HOT_TIP_THRESHOLD_HOME_LOW_SCORE = 50.0f;
    public static final float HOT_TIP_THRESHOLD_HOME_HIGH_SCORE = 100.0f;
    public static final float HOT_TIP_THRESHOLD_BTTS = 100.0f;
    public static final float HOT_TIP_THRESHOLD_AWAY_WIN = 400.0f;
    public static final float HOT_TIP_THRESHOLD_AWAY_LOW_SCORE = 50.0f;
    public static final float HOT_TIP_THRESHOLD_AWAY_HIGH_SCORE = 100.0f;

    private static final String DATA_DIR_NAME = "data";

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File DATA_DIR = new File(CWD, DATA_DIR_NAME);


    // set this to use a local sample fixture file, instead of trying to retrieve from remote site
    public static final boolean DEV_MODE = false;
    public static final File SAMPLE_FIXTURE_DEV_MODE_FILE=new File(DATA_DIR, "sample_fixtures_used_for_dev_mode.csv");

    // use this flag to use this weeks fixtures that have already been played, useful if hanging around for Friday in order to get some useful runs....
    // this flag means we'll go off and get the fixture data from remote website - instead of using the local hard-coded fixture file used when DEV_MODE flag is set
    // note: if this is true, then set DEV_MODE to false, otherwise local fixtures sample file will be used ...
    public static final boolean DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES = false;


    /** Set this to only list predictions for todays games */
    //public static final boolean ONLY_TODAYS_GAMES = true;
    //public static final boolean ONLY_TODAYS_GAMES = false;

    /** Set to true if use ALL divisions*/
    public static final boolean USE_ALL_LEAGUES = false;

    /** Set to true if use UK divisions, false to use EURO leagues */
    public static final boolean USE_UK_LEAGUES = true;
    //public static final boolean USE_UK_LEAGUES = false;

    /**
     * English League
     */
    public static final List<Division> EPL = Arrays.asList(Division.England_Premier_League);
    public static final List<Division> ECH = Arrays.asList(Division.England_Championship);
    public static final List<Division> EL1 = Arrays.asList(Division.England_League_1);
    public static final List<Division> EL2 = Arrays.asList(Division.England_League_2);
    public static final List<Division> ECF = Arrays.asList(Division.England_Conference);

    public static final List<Division> ENG_TOP2 = Stream.of(EPL, ECH)
            .flatMap(x -> x.stream()).collect(Collectors.toList());

    public static final List<Division> ENG_DIVISIONS = Stream.of(EPL, ECH, EL1, EL2, ECF)
            .flatMap(x -> x.stream()).collect(Collectors.toList());

    /**
     * Scottish League
     */
    public static final List<Division> SCO_PL = Arrays.asList(Division.Scotland_Premier_League);
    public static final List<Division> SCO_CH = Arrays.asList(Division.Scotland_Championship);
    public static final List<Division> SCO_L1 = Arrays.asList(Division.Scotland_Div_1);
    public static final List<Division> SCO_L2 = Arrays.asList(Division.Scotland_Div_2);

    public static final List<Division> SCOT_DIVISIONS = Stream.of(SCO_PL, SCO_CH, SCO_L1, SCO_L2)
            .flatMap(x -> x.stream()).collect(Collectors.toList());


    public static final List<Division> UK_DIVISIONS = Stream.of(ENG_DIVISIONS, SCOT_DIVISIONS)
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    /**
     * European Leagues
     */
    public static final List<Division> GER = Arrays.asList(Division.Germany_Bundesliga_1);
    public static final List<Division> SPA = Arrays.asList(Division.Spain_La_Liga);
    public static final List<Division> ITA = Arrays.asList(Division.Italy_Serie_A);
    public static final List<Division> FRA = Arrays.asList(Division.France_Ligue_1);
    public static final List<Division> HOL = Arrays.asList(Division.Holland_Eridivisie);
    public static final List<Division> BEL = Arrays.asList(Division.Germany_Bundesliga_1);
    public static final List<Division> POR = Arrays.asList(Division.Germany_Bundesliga_1);


    public static final List<Division> EURO_DIVISIONS = Stream.of(GER, SPA, ITA, FRA, HOL, BEL, POR)
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    public static final List<Division> EURO_ELITE = Stream.of(EPL, GER, SPA, ITA, FRA)
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    public static final List<Division> ALL_DIVISIONS = Stream.of(UK_DIVISIONS, EURO_DIVISIONS)
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    public static final boolean SHOW_DETAILED_STATS = false;

    //public static final String SEASON_TO_USE = System.getProperty("bluebird.season", "1920");

    public final static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    private static final Logger logger = LogManager.getLogger("AppConstants");

    //public static final int CURRENT_FORM_GAMES = 4;

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
