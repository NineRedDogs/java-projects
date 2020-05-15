package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.SortedSet;

public abstract class HomeOrAwayTable extends Table {

    private static final Logger logger = LogManager.getLogger("HomeOrAwayTable");

    private final Table homeAndAwayTable;

    public HomeOrAwayTable(String tableName, Division division, Config cfg, SortedSet<String> teams, Table homeAndAwayTable) {
        super(tableName, division, cfg, teams);
        this.homeAndAwayTable = homeAndAwayTable;
    }

    protected Table getHomeAndAwayTable() {
        return homeAndAwayTable;
    }
}
