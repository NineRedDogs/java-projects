package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BTTS extends ForecastType {

    private static final Logger logger = LogManager.getLogger("BTTS");
    private static final float HOME_SCORE_THRESHOLD = 1.0f;
    private static final float AWAY_SCORE_THRESHOLD = 1.0f;
    private static final int BTTS_THRESHOLD = 80;

    public BTTS() {
        super();
    }

    @Override
    protected int calcForecastScore(FixtureData fd) {
        TeamForecastData ht = fd.getForecastData().getHtData();
        TeamForecastData at = fd.getForecastData().getAtData();

        int btts1 = getBtts(ht.getTeamForecastData(TeamForecastData.FORM_VENUE), at.getTeamForecastData(TeamForecastData.FORM_VENUE));
        int btts2 = getBtts(ht.getTeamForecastData(TeamForecastData.FORM_GENERAL), at.getTeamForecastData(TeamForecastData.FORM_GENERAL));
        int btts2a = Math.round(((float) btts2 * FORM_GENERAL_MULTIPLIER));
        int btts3 = getBtts(ht.getTeamForecastData(TeamForecastData.SEASON_VENUE), at.getTeamForecastData(TeamForecastData.SEASON_VENUE));
        int btts3a = Math.round(((float) btts3 * SEASON_VENUE_MULTIPLIER));

        int bttsFinalScore = (AppConstants.JUST_USE_VENUE_FORM) ? btts1 : (btts1 + btts2a + btts3a);

        //logger.debug("BTTS *** " + bttsFinalScore + " ***");
        return bttsFinalScore;
    }

    private int getBtts(TableEntry homeTeam, TableEntry awayTeam) {
        int bttsForecast = 0;

        //logger.debug(homeTeam.fullString());
        //logger.debug(awayTeam.fullString());

        // calc HT likelyhood score value - (GF/P + GA/P(opp)) / 2
        float htScoreRaw = (((float) homeTeam.getGoalsFor() / homeTeam.getGamesPlayed() +
                          (float) awayTeam.getGoalsAgainst() / awayTeam.getGamesPlayed()) / 2);
        float htScore = (htScoreRaw > HOME_SCORE_THRESHOLD) ? htScoreRaw : 0.0f;

        // calc AT likelyhood score value - (GF/P + GA/P(opp)) / 2
        float atScoreRaw = (((float) awayTeam.getGoalsFor() / awayTeam.getGamesPlayed() +
                          (float) homeTeam.getGoalsAgainst() / homeTeam.getGamesPlayed()) / 2);
        float atScore = (atScoreRaw > AWAY_SCORE_THRESHOLD) ? atScoreRaw : 0.0f;


        // calc btts based on btts history
        float bttsHistoric = ((((float) homeTeam.getBttsGames() / homeTeam.getGamesPlayed()) +
                       ((float) awayTeam.getBttsGames() / awayTeam.getGamesPlayed())) / 2);
//        logger.debug(("btts: : " + bttsHistoric));

        // calc games scored-in value
        float gsH = (((float) homeTeam.getGamesScoredUs() / homeTeam.getGamesPlayed()) +
                ((float) awayTeam.getGamesScoredOppo() / awayTeam.getGamesPlayed()));
        float gsA = (((float) awayTeam.getGamesScoredUs() / awayTeam.getGamesPlayed()) +
                ((float) homeTeam.getGamesScoredOppo() / homeTeam.getGamesPlayed()));
        float gs = ((float) (gsH + gsA) / 4);
//        logger.debug("gs : " + gs);

        float bttsMult = ((float) (bttsHistoric + gs) / 2);
        float bttsForecastRaw = ((float) htScore * atScore * bttsMult);
        bttsForecast = Math.round(bttsForecastRaw * 100);
//        logger.debug("--------------------------");
//        logger.debug("btts (raw) : " + bttsForecastRaw);
//        logger.debug("btts       : " + bttsForecast);
//        logger.debug("--------------------------");

        return bttsForecast;
    }


}
