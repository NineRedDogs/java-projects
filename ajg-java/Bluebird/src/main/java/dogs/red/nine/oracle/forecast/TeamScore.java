package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class TeamScore extends ForecastType {

    private static final Logger logger = LogManager.getLogger("TeamScore");

    public TeamScore() {
        super();
    }

    protected int getHighScoreRating(TeamForecastData team1, TeamForecastData team2) {
        int hw1 = getHiScore(team1.getTeamForecastData(TeamForecastData.FORM_VENUE), team2.getTeamForecastData(TeamForecastData.FORM_VENUE));
        int hw2 = getHiScore(team1.getTeamForecastData(TeamForecastData.FORM_GENERAL), team2.getTeamForecastData(TeamForecastData.FORM_GENERAL));
        int hw2a = Math.round(((float) hw2 * FORM_GENERAL_MULTIPLIER));
        int hw3 = getHiScore(team1.getTeamForecastData(TeamForecastData.SEASON_VENUE), team2.getTeamForecastData(TeamForecastData.SEASON_VENUE));
        int hw3a = Math.round(((float) hw3 * SEASON_VENUE_MULTIPLIER));

        int hwFinalScore = (AppConstants.JUST_USE_VENUE_FORM) ? hw1 : (hw1 + hw2a + hw3a);

        //logger.debug("highScore overall *** " + hwFinalScore + " ***");
        return hwFinalScore;
    }

    private int getHiScore(TableEntry team1, TableEntry team2) {
        int hiScoreForecast = 0;

        //logger.debug(team1.fullString());
        //logger.debug(team2.fullString());

        // only perform calcs if we have two teams worth of data
        if ((team1 == null) || (team2 == null)) {
            return hiScoreForecast;
        }

        // calc team1 likelyhood score value - (GF/P + GA/P(opp)) / 2
        float t1ScoreRaw = (((float) team1.getGoalsFor() / team1.getGamesPlayed() +
                          (float) team2.getGoalsAgainst() / team2.getGamesPlayed()) / 2);
        //logger.debug("Team Score : " + t1ScoreRaw);

        // calc toWin based on quality number
        float qualityDiff = (team1.getQualityRating() - team2.getQualityRating());
        //logger.debug(("qualityDiff: " + qualityDiff));

        hiScoreForecast = Math.round((t1ScoreRaw + qualityDiff) * 100);
        //logger.debug("hiScore : " + hiScoreForecast);

        return hiScoreForecast;
    }


}
