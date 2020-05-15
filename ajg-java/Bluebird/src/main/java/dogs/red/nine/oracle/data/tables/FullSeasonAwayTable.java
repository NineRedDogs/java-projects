package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.SortedSet;

public class FullSeasonAwayTable extends HomeOrAwayTable {
    private static final Logger logger = LogManager.getLogger("FullSeasonAwayTable");

    protected FullSeasonAwayTable(Division division, Config cfg, SortedSet<String> teams, FullSeasonTable fullSeasonTable) {
        super("Full Away Table", division, cfg, teams, fullSeasonTable);
        for (String team : teams) {
            addEntry(team, new FullSeasonAwayTableEntry(team));
        }
    }

    public void generateTable(List<MatchData> matches) {
        for (MatchData match : matches) {
            getEntry(match.getAwayTeam()).addResult(match, getHomeAndAwayTable().getEntry(match.getHomeTeam()));
        }
        table = sortTable();
    }
}
