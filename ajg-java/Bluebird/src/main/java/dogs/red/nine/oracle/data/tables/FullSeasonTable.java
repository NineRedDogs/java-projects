package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FullSeasonTable extends Table {
    private static final Logger logger = LogManager.getLogger("FullSeasonTable");

    protected FullSeasonTable(Division division, SortedSet<String> teams) {
        super("Full Table", division, teams);
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
