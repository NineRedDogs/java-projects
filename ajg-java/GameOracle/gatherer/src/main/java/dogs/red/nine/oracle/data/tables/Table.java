package dogs.red.nine.oracle.data.tables;

import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Table {

    private static final Logger logger = LogManager.getLogger("Table");
    private final String tableName;
    private SortedMap<String, TableEntry> table = new ConcurrentSkipListMap<String, TableEntry>();

    protected Table(String tableName) {
        this.tableName = tableName;
    }

    public void displayTable() {
        logger.debug("Displaying table [" + tableName + "]");
        logger.debug(TableEntry.formattedHeaders);
        table.forEach((key, value) -> logger.debug(" " + key + "  " + value));
    }






}
