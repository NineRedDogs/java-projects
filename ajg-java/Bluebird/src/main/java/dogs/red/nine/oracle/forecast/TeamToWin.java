package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class TeamToWin extends ForecastType {

    private static final Logger logger = LogManager.getLogger("TeamToWin");

    private static final int TO_WIN_THRESHOLD = 100;

    public TeamToWin() {
        super();
    }

    protected int getToWinScore(TeamForecastData team1, TeamForecastData team2) {
        int hw1 = getToWin(team1.getTeamForecastData(TeamForecastData.FORM_VENUE), team2.getTeamForecastData(TeamForecastData.FORM_VENUE));
        int hw2 = getToWin(team1.getTeamForecastData(TeamForecastData.FORM_GENERAL), team2.getTeamForecastData(TeamForecastData.FORM_GENERAL));
        int hw2a = Math.round(((float) hw2 * FORM_GENERAL_MULTIPLIER));
        int hw3 = getToWin(team1.getTeamForecastData(TeamForecastData.SEASON_VENUE), team2.getTeamForecastData(TeamForecastData.SEASON_VENUE));
        int hw3a = Math.round(((float) hw3 * SEASON_VENUE_MULTIPLIER));

        int hwFinalScore = (AppConstants.JUST_USE_VENUE_FORM) ? hw1 : (hw1 + hw2a + hw3a);

        //logger.debug("To Win *** " + hwFinalScore + " ***");
        return hwFinalScore;
    }

    private int getToWin(TableEntry team1, TableEntry team2) {
        int toWinForecast = 0;

        //logger.debug(team1.fullString());
        //logger.debug(team2.fullString());

        // calc team1 likelyhood score value - (GF/P + GA/P(opp)) / 2
        float t1ScoreRaw = (((float) team1.getGoalsFor() / team1.getGamesPlayed() +
                          (float) team2.getGoalsAgainst() / team2.getGamesPlayed()) / 2);

        // calc team2 likelyhood score value - (GF/P + GA/P(opp)) / 2
        float t2ScoreRaw = (((float) team2.getGoalsFor() / team2.getGamesPlayed() +
                          (float) team1.getGoalsAgainst() / team1.getGamesPlayed()) / 2);

        float diff = t1ScoreRaw - t2ScoreRaw;
        //logger.debug("Score diff : " + diff);

        // calc toWin based on quality number
        float qualityDiff = (team1.getQualityRating() - team2.getQualityRating());
        //logger.debug(("qualityDiff: " + qualityDiff));

        toWinForecast = Math.round((diff + qualityDiff) * 100);
        //logger.debug("toWin score : " + toWinForecast);

        return toWinForecast;
    }


}
