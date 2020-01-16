package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;

import java.util.*;
import java.util.stream.Collectors;

public class FullSeasonTable extends Table {
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
        displayTable();
    }

    private SortedMap<String, TableEntry> sortTable() {
        return table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, TreeMap::new));

    }
}
