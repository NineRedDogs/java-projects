package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class Table {

    private static final Logger logger = LogManager.getLogger("Table");
    protected final String tableName;
    protected final Division division;

    //protected SortedMap<String, TableEntry> table = new ConcurrentSkipListMap<String, TableEntry>();
    protected Map<String, TableEntry> table = new LinkedHashMap<String, TableEntry>();

    public abstract void generateTable(List<MatchData> matchData);

    protected Table(String tableName, Division division, SortedSet<String> teams) {
        this.tableName = tableName;
        this.division = division;
    }



    public void displayTable(final String title) {
        logger.debug("Displaying table [" + tableName + "] - " + title);
        logger.debug(TableEntry.formattedHeaders);
        table.forEach((key, value) -> logger.debug(" " + key + "  " + value));
    }






}
