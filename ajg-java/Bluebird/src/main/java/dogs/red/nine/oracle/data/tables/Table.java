package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Table {

    private static final Logger logger = LogManager.getLogger("Table");
    protected final String tableName;
    protected final Division division;
    private final Config config;
    protected Map<String, TableEntry> table = new LinkedHashMap<String, TableEntry>();

    public abstract void generateTable(List<MatchData> matchData);

    protected Table(String tableName, Division division, Config cfg, SortedSet<String> teams) {
        this.tableName = tableName;
        this.division = division;
        this.config = cfg;
    }

    protected Map<String, TableEntry> sortTable() {
        Map<String, TableEntry> x = table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return x;
    }

    public int getPosition(final String team) {
        return new ArrayList<String>(table.keySet()).indexOf(team) + 1;
    }

    public TableEntry getTeamData(final String team) {
        return table.get(team);
    }

    public void displayTable(final String title) {
        logger.debug("\n\nDisplaying table [" + tableName + "] - " + title);
        logger.debug(TableEntry.formattedHeaders);
        table.forEach((key, value) -> logger.debug(" " + String.format("%-20s", key).replace(' ', '.') + "  " + value));
    }

    public Map<String, TableEntry> getTable() {
        return table;
    }

    protected void addEntry(String team, TableEntry tableEntry) {
        table.put(team, tableEntry);
    }



    public TableEntry getEntry(final String teamName) {
        return table.get(teamName);
    }

    protected Config getConfig() {
        return config;
    }
}
