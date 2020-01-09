package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;

import java.util.List;
import java.util.SortedSet;

public class FullSeasonTable extends Table {
    protected FullSeasonTable(Division division, SortedSet<String> teams) {
        super("Full Table", division, teams);
    }

    public void generateTable(List<MatchData> matches) {

        for (MatchData match : matches) {

        }

    }
}
