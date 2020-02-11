package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class HomeOrAwayTable extends Table {

    private static final Logger logger = LogManager.getLogger("HomeOrAwayTable");

    private final Table homeAndAwayTable;

    public HomeOrAwayTable(String tableName, Division division, SortedSet<String> teams, Table homeAndAwayTable) {
        super(tableName, division, teams);
        this.homeAndAwayTable = homeAndAwayTable;
    }

    protected Table getHomeAndAwayTable() {
        return homeAndAwayTable;
    }
}
