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
    private final SortedMap<Date, Table> fullHomeTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> fullAwayTables = new ConcurrentSkipListMap<Date, Table>();



    public void generateTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {


        // generate all the full season tables - one for each date ...
        Date currDate = matchData.get(0).getDate();
        List<MatchData> tableMatches = new ArrayList<MatchData>();


        // create a new matchData structure
        for (MatchData match : matchData) {
//            logger.debug("======================================================");
//            logger.debug(" comparing currDate:" + currDate + " vs match date:" + match.getDate() + " compare: " +
//                    currDate.compareTo(match.getDate()));

            if (currDate.compareTo(match.getDate()) < 0) {
                // current match has a new date, so generate table for all matches so far
                genTable(division, teams, tableMatches, currDate);



                // move curr date on
                currDate = match.getDate();
//                logger.debug(" moving up currDate:" + currDate);
            }
            tableMatches.add(match);
        }

        // record final table
        genTable(division, teams, tableMatches, currDate);

    }

    private void genTable(Division division, SortedSet<String> teams, List<MatchData> tableMatches, Date currDate) {
        logger.debug("storing table for date : " + currDate);

        FullSeasonTable fullSeasonTable = new FullSeasonTable(division, teams);
        fullSeasonTable.generateTable(tableMatches);
        fullTables.put(currDate, fullSeasonTable);
        //fullSeasonTable.displayTable(currDate.toString());

        FullSeasonHomeTable fullSeasonHomeTable = new FullSeasonHomeTable(division, teams);
        fullSeasonHomeTable.generateTable(tableMatches);
        fullHomeTables.put(currDate, fullSeasonHomeTable);
        //fullSeasonHomeTable.displayTable(currDate.toString());


        FullSeasonAwayTable fullSeasonAwayTable = new FullSeasonAwayTable(division, teams);
        fullSeasonAwayTable.generateTable(tableMatches);
        fullAwayTables.put(currDate, fullSeasonAwayTable);
        fullSeasonAwayTable.displayTable(currDate.toString());
    }


}
