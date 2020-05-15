package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.SortedSet;

public class FullSeasonTable extends Table {
    private static final Logger logger = LogManager.getLogger("FullSeasonTable");

    protected FullSeasonTable(Division division, Config cfg, SortedSet<String> teams) {
        super("Full Table", division, cfg, teams);
        for (String team : teams) {
            addEntry(team, new FullSeasonTableEntry(team));
        }
    }

    public void generateTable(List<MatchData> matches) {

        for (MatchData match : matches) {
            getEntry(match.getHomeTeam()).addResult(match, getEntry(match.getAwayTeam()));
            getEntry(match.getAwayTeam()).addResult(match, getEntry(match.getHomeTeam()));
        }
        table = sortTable();
    }


}
