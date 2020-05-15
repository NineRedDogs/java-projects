package dogs.red.nine.oracle;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.forecast.Forecaster;
import dogs.red.nine.oracle.gatherer.Gatherer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "Bluebird-CLI",
        description = "Football predictions generator",
        version = "bluebird 0.1.0",
        mixinStandardHelpOptions = true
)
public class Oracle implements Callable<Integer> {
    @CommandLine.Option(names = { "-l", "--leagues" }, paramLabel = "LEAGUES", description = "the leagues to use", defaultValue = "ALL")
    String leaguesToUse;

    @CommandLine.Option(names = { "-cf", "--current-form-games" }, paramLabel = "CURRENT_FORM_GAMES", description = "number of games to use when calculating current form", defaultValue = "4")
    int numCurrentFormGames;

    @CommandLine.Option(names = { "-s", "--season" }, paramLabel = "SEASON", description = "what season to generate predictions for (usually current season)", defaultValue = "1920")
    String season;

    @CommandLine.Option(names = { "-t", "--just-todays-games" }, paramLabel = "JUST_TODAYS_GAMES", description = "only use todays games for predictions", defaultValue = "false")
    boolean justTodaysGames;

    private static final Logger logger = LogManager.getLogger("Oracle");

    /** responsible for gathering data, e.g. getting latest results data and also grabbing the fixtures */
    private Gatherer dataGatherer;

    /** responsible for running the forecasting algorithms and identifying the tips. */
    private Forecaster forecaster;

    /** subset of the full set of current fixtures, containing the ones 'chosen' by the predictor algorithms */
    private final List<FixtureData> forecastFixtures = new ArrayList<FixtureData>();

    public Oracle() { }


    public void forecast()  throws IOException {
        // get config
        Config cfg = new Config(leaguesToUse, numCurrentFormGames, season, justTodaysGames);
        logger.info("config being used : " + cfg);


        dataGatherer = new Gatherer(cfg);
        forecaster = new Forecaster(dataGatherer.getTableManager());
        forecaster.forecast(dataGatherer.getFixtures());
        logger.info("\n\nsee logs/predictions.log for predictions ....\n\n\n");
    }


    public static void main(String[] args) throws IOException, ParseException {
        int exitCode = new CommandLine(new Oracle()).execute(args);
        System.exit(exitCode);
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        forecast();
        return 0;
    }

}
