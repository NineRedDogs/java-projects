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

    private Map<String, TableEntry> sortTable() {
        Map<String, TableEntry> x = table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return x;
    }
}
