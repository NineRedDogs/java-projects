package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;

public class TableGenerator {

    private static final Logger logger = LogManager.getLogger("TableGenerator");

    public void generateTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        Date currDate = matchData.get(0).getDate();

        // create a new matchData structure
        for (MatchData match : matchData) {
            // if current match date is diff to currDate, then
            //     create a new fullseason table with temp match data structure
            // endif
            // copy match data for date to the new structure
        }


    }
}
