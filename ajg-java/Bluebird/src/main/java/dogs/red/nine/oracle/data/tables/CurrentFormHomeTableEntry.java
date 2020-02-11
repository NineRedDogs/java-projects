package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CurrentFormHomeTableEntry extends TableEntry {

    private static final Logger logger = LogManager.getLogger("CurrentFormHomeTableEntry");


    public CurrentFormHomeTableEntry(String teamName) {
        super(teamName);
    }

    public void addResult(final MatchData result, final TableEntry otherTeam) {
        if (getTeamName().equalsIgnoreCase(result.getHomeTeam())) {
            // team is home team
            add(result.getHomeTeamScore(), result.getAwayTeamScore(), otherTeam);
        } else {
            logger.debug("current team not home in given match !!!");
        }
    }


}
