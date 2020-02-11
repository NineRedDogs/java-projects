package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.SortedSet;

public class FullSeasonHomeTable extends HomeOrAwayTable {
    private static final Logger logger = LogManager.getLogger("FullSeasonHomeTable");

    protected FullSeasonHomeTable(Division division, SortedSet<String> teams, FullSeasonTable fullSeasonTable) {
        super("Full Home Table", division, teams, fullSeasonTable);
        for (String team : teams) {
            addEntry(team, new FullSeasonHomeTableEntry(team));
        }
    }

    public void generateTable(List<MatchData> matches) {
        for (MatchData match : matches) {
            getEntry(match.getHomeTeam()).addResult(match, getHomeAndAwayTable().getEntry(match.getAwayTeam()));
        }
        table = sortTable();
    }
}
