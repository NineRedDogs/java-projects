package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FullSeasonMeritTableEntry extends TableEntry {

    private static final Logger logger = LogManager.getLogger("FullSeasonMeritTableEntry");

    private int meritScore;

    public int getMeritScore() {
        return meritScore;
    }


    public FullSeasonMeritTableEntry(String teamName) {
        super(teamName);
    }

    @Override
    public void addResult(MatchData result) {
        addResult(result, 0);
    }

    public void addResult(final MatchData result, int theirMeritScore) {
        if (getTeamName().equalsIgnoreCase(result.getHomeTeam())) {
            // team is home team
            add(result.getHomeTeamScore(), result.getAwayTeamScore(), theirMeritScore);

        } else if (getTeamName().equalsIgnoreCase(result.getAwayTeam())) {
            // team is away team
            add(result.getAwayTeamScore(), result.getHomeTeamScore(), theirMeritScore);
        } else {
            logger.debug("current team not home OR away in given match !!!");
        }
    }

    protected void add(int ourScore, int otherTeamScore, int theirMeritScore) {
        incrementGamesPlayed();
        if (ourScore > otherTeamScore) {
            incrementGamesWon();
            incrementPoints(3);
        } else if (ourScore == otherTeamScore) {
            incrementGamesDrawn();
            incrementPoints(1);
        } else {
            incrementGamesLost();
        }
        if ((ourScore > 0) && (otherTeamScore > 0)) {
            incrementBtts();
        }
        if (ourScore == 0) {
            incrementCleanSheetUs();
        } else {
            incrementGamesScoredUs();
            if (ourScore > 1) {
                incrementHighScoreUs();
            }
        }
        if (otherTeamScore == 0) {
            incrementCleanSheetOppo();
        } else {
            incrementGamesScoredOppo();
            if (otherTeamScore > 1) {
                incrementHighScoreOppo();
            }
        }

        incrementGoalsFor(ourScore);
        incrementGoalsAgainst(otherTeamScore);

        incrementMeritScore();
    }



    @Override
    public String toString() {
        return super.toString() + " " + formatInt(getMeritScore());
    }



}
