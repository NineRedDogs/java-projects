package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FullSeasonTableEntry extends TableEntry {

    private static final Logger logger = LogManager.getLogger("FullSeasonTableEntry");


    public FullSeasonTableEntry(String teamName) {
        super(teamName);
    }

    public void addResult(final MatchData result) {
        if (getTeamName().equalsIgnoreCase(result.getHomeTeam())) {
            // team is home team
            add(result.getHomeTeamScore(), result.getAwayTeamScore());

        } else if (getTeamName().equalsIgnoreCase(result.getAwayTeam())) {
            // team is away team
            add(result.getAwayTeamScore(), result.getHomeTeamScore());
        } else {
            logger.debug("current team not home OR away in given match !!!");
        }
    }



}
