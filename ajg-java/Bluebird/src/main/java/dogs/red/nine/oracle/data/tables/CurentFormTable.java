package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.SortedSet;

public class CurentFormTable extends Table {
    private static final Logger logger = LogManager.getLogger("FullSeasonTable");

    protected CurentFormTable(Division division, SortedSet<String> teams) {
        super("Full Table", division, teams);
        for (String team : teams) {
            table.put(team, new FullSeasonTableEntry(team));
        }
    }

    public void generateTable(List<MatchData> matches) {

        for (MatchData match : matches) {
            table.get(match.getHomeTeam()).addResult(match);
            table.get(match.getAwayTeam()).addResult(match);
        }
        table = sortTable();
    }


}
