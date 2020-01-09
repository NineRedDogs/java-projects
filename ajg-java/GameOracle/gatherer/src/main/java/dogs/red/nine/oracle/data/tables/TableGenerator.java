package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class TableGenerator {

    private static final Logger logger = LogManager.getLogger("TableGenerator");
    private final SortedMap<Date, Table> fullTables = new ConcurrentSkipListMap<Date, Table>();


    public void generateTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        Date currDate = matchData.get(0).getDate();
        List<MatchData> tableMatches = new ArrayList<MatchData>();

        // create a new matchData structure
        for (MatchData match : matchData) {
            if (currDate != match.getDate()) {
                Table fullTableUntilCurrDate = FullSeasonTable.generate(division, matchData, teams);
                fullTables.put(currDate, fullTableUntilCurrDate);
            }
            tableMatches.add(match);
        }

    }
}
