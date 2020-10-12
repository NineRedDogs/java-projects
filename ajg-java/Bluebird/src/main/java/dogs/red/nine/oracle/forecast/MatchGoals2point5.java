package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class MatchGoals2point5 extends ForecastType {

    private static final Logger logger = LogManager.getLogger("MatchGoals2point5");

    public MatchGoals2point5() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        return getMatchScoreRating(ht, at);
    }

    protected int getMatchScoreRating(TeamForecastData team1, TeamForecastData team2) {
        int hw1 = getMatchScore(team1.getTeamForecastData(TeamForecastData.FORM_VENUE), team2.getTeamForecastData(TeamForecastData.FORM_VENUE));
        int hw2 = getMatchScore(team1.getTeamForecastData(TeamForecastData.FORM_GENERAL), team2.getTeamForecastData(TeamForecastData.FORM_GENERAL));
        int hw2a = Math.round(((float) hw2 * FORM_GENERAL_MULTIPLIER));
        int hw3 = getMatchScore(team1.getTeamForecastData(TeamForecastData.SEASON_VENUE), team2.getTeamForecastData(TeamForecastData.SEASON_VENUE));
        int hw3a = Math.round(((float) hw3 * SEASON_VENUE_MULTIPLIER));

        int hwFinalScore = (AppConstants.JUST_USE_VENUE_FORM) ? hw1 : (hw1 + hw2a + hw3a);

        //logger.debug("match highScore overall *** " + hwFinalScore + " ***");
        return hwFinalScore;
    }

    private int getMatchScore(TableEntry team1, TableEntry team2) {
        int matchScoreForecast = 0;

//        logger.debug(team1.fullString());
//        logger.debug(team2.fullString());

        // only perform calcs if we have two teams worth of data
        if ((team1 == null) || (team2 == null)) {
            return matchScoreForecast;
        }

        // calc team1 likelihood score value - (GF/P + GA/P(opp)) / 2
        float t1ScoreRaw = (((float) team1.getGoalsFor() / team1.getGamesPlayed() +
                          (float) team2.getGoalsAgainst() / team2.getGamesPlayed()) / 2);
        float t1ScoreWithQuality = (t1ScoreRaw * (team1.getMeritRate() - team2.getMeritRate()));
//        logger.debug("Team1 Score : " + t1ScoreRaw);
//        logger.debug("Team1 Score (with Qual) : " + t1ScoreWithQuality);
        if (t1ScoreWithQuality < 0) {
            t1ScoreWithQuality = 0.0f;
//            logger.debug("Team1 Score (with Qual) neg so setting to : " + t1ScoreWithQuality);
        }

        // calc team2 likelihood score value - (GF/P + GA/P(opp)) / 2
        float t2ScoreRaw = (((float) team2.getGoalsFor() / team2.getGamesPlayed() +
                (float) team1.getGoalsAgainst() / team1.getGamesPlayed()) / 2);
        float t2ScoreWithQuality = (t2ScoreRaw * (team2.getMeritRate() - team1.getMeritRate()));
//        logger.debug("Team2 Score : " + t2ScoreRaw);
//        logger.debug("Team2 Score (with Qual) : " + t2ScoreWithQuality);
        if (t2ScoreWithQuality < 0) {
            t2ScoreWithQuality = 0.0f;
//            logger.debug("Team2 Score (with Qual) neg so setting to : " + t2ScoreWithQuality);
        }

        matchScoreForecast = Math.round((t1ScoreRaw + t2ScoreRaw) * 100);
//        logger.debug("hiScore : " + matchScoreForecast);

        return matchScoreForecast;
    }



}
