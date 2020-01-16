package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FullSeasonAwayTableEntry extends TableEntry {

    private static final Logger logger = LogManager.getLogger("FullSeasonAwayTableEntry");


    public FullSeasonAwayTableEntry(String teamName) {
        super(teamName);
    }

    public void addResult(final MatchData result) {
        if (getTeamName().equalsIgnoreCase(result.getAwayTeam())) {
            // team is away team
            add(result.getAwayTeamScore(), result.getHomeTeamScore());
        } else {
            logger.debug("current team not away in given match !!!");
        }
    }

    private void add(int ourScore, int otherTeamScore) {
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
        incrementGoalsFor(ourScore);
        incrementGoalsAgainst(otherTeamScore);

    }

}
