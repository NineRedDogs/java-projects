package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class DivisionTableManager {

    private static final Logger logger = LogManager.getLogger("DivisionTableManager");
    private final Config config;
    private final SortedMap<LocalDate, Table> fullTables = new ConcurrentSkipListMap<>();
    private final SortedMap<LocalDate, Table> fullHomeTables = new ConcurrentSkipListMap<>();
    private final SortedMap<LocalDate, Table> fullAwayTables = new ConcurrentSkipListMap<>();
    private final SortedMap<LocalDate, Table> formTables = new ConcurrentSkipListMap<>();
    private final SortedMap<LocalDate, Table> formHomeTables = new ConcurrentSkipListMap<>();
    private final SortedMap<LocalDate, Table> formAwayTables = new ConcurrentSkipListMap<>();

    public DivisionTableManager(Config cfg) {
        this.config = cfg;
    }

    public void generateTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {
        generateFullSeasonTables(division, matchData, teams);
        generateFormTables(division, matchData, teams);
    }


    public void displayCurrentTables() {
        if (fullTables.size() > 0) {
            fullTables.get(fullTables.lastKey()).displayTable("Current League Table");
        }
        if (fullHomeTables.size() > 0) {
            fullHomeTables.get(fullHomeTables.lastKey()).displayTable("Current Home League Table");
        }
        if (fullAwayTables.size() > 0) {
            fullAwayTables.get(fullAwayTables.lastKey()).displayTable("Current Away League Table");
        }

        if (formTables.size() > 0) {
            formTables.get(formTables.lastKey()).displayTable("Current Form Table");
        }
        if (formHomeTables.size() > 0) {
            formHomeTables.get(formHomeTables.lastKey()).displayTable("Current Form Home Table");
        }
        if (formAwayTables.size() > 0) {
            formAwayTables.get(formAwayTables.lastKey()).displayTable("Current Form Away Table");
        }
    }

    private void generateFullSeasonTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        // but only run if there's at least one match date ...
        if (matchData.size() > 0) {
            LocalDate currDate = matchData.get(0).getDate();
            List<MatchData> tableMatches = new ArrayList<MatchData>();

            // create a new matchData structure
            for (MatchData match : matchData) {
                if (currDate.compareTo(match.getDate()) < 0) {
                    // current match has a new date, so generate table for all matches so far
                    genFullSeasonTable(division, teams, tableMatches, currDate);

                    // move curr date on
                    currDate = match.getDate();
                }
                tableMatches.add(match);
            }

            // record final table
            genFullSeasonTable(division, teams, tableMatches, currDate);
        }
    }

    private void genFullSeasonTable(Division division, SortedSet<String> teams, List<MatchData> tableMatches, LocalDate currDate) {
        FullSeasonTable fullSeasonTable = new FullSeasonTable(division, config, teams);
        fullSeasonTable.generateTable(tableMatches);
        fullTables.put(currDate, fullSeasonTable);
        //fullSeasonTable.displayTable(currDate.toString());

        FullSeasonHomeTable fullSeasonHomeTable = new FullSeasonHomeTable(division, config, teams, fullSeasonTable);
        fullSeasonHomeTable.generateTable(tableMatches);
        fullHomeTables.put(currDate, fullSeasonHomeTable);
        //fullSeasonHomeTable.displayTable(currDate.toString());

        FullSeasonAwayTable fullSeasonAwayTable = new FullSeasonAwayTable(division, config, teams, fullSeasonTable);
        fullSeasonAwayTable.generateTable(tableMatches);
        fullAwayTables.put(currDate, fullSeasonAwayTable);
        //fullSeasonAwayTable.displayTable(currDate.toString());

    }


    private void generateFormTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        // but only run if there's at least one match date ...
        if (matchData.size() > 0) {
            // init with a day before the first match date
            LocalDate currDate = matchData.get(0).getDate();
            LocalDate mostRecentMatchDate = null;
            List<MatchData> tableMatches = new ArrayList<MatchData>();

            // create a new matchData structure
            for (MatchData match : matchData) {
                if (currDate.compareTo(match.getDate()) < 0) {
                    // current match has a new date, so generate table for all matches so far
                    genFormTable(division, teams, tableMatches, currDate);

                    // update curr date on
                    currDate = match.getDate();
                }
                tableMatches.add(match);
                mostRecentMatchDate = match.getDate();
            }

            // record final table
            genFormTable(division, teams, tableMatches, mostRecentMatchDate);
        }
    }

    private void genFormTable(Division division, SortedSet<String> teams, List<MatchData> tableMatches, LocalDate currDate) {
        CurrentFormTable currentFormTable = new CurrentFormTable(division, config, teams);
        currentFormTable.generateTable(tableMatches);
        formTables.put(currDate, currentFormTable);
        //currentFormTable.displayTable(currDate.toString());

        CurrentFormHomeTable currentFormHomeTable = new CurrentFormHomeTable(division, config, teams, currentFormTable);
        currentFormHomeTable.generateTable(tableMatches);
        formHomeTables.put(currDate, currentFormHomeTable);
        //currentFormHomeTable.displayTable(currDate.toString());

        CurrentFormAwayTable currentFormAwayTable = new CurrentFormAwayTable(division, config, teams, currentFormTable);
        currentFormAwayTable.generateTable(tableMatches);
        formAwayTables.put(currDate, currentFormAwayTable);
        //currentFormAwayTable.displayTable(currDate.toString());
    }


    public TableEntry getHomeFormData(String homeTeam) {
        return formHomeTables.get(formHomeTables.lastKey()).getTeamData(homeTeam);
    }

    public TableEntry getAwayFormData(String awayTeam) {
        return formAwayTables.get(formAwayTables.lastKey()).getTeamData(awayTeam);
    }

    public TableEntry getFormData(String team) {
        return formTables.get(formTables.lastKey()).getTeamData(team);
    }

    public TableEntry getHomeSeasonData(String homeTeam) {
        return fullHomeTables.get(fullHomeTables.lastKey()).getTeamData(homeTeam);
    }

    public TableEntry getAwaySeasonData(String awayTeam) {
        return fullAwayTables.get(fullAwayTables.lastKey()).getTeamData(awayTeam);
    }

    public TableEntry getSeasonData(String team) {
        return fullTables.get(fullTables.lastKey()).getTeamData(team);
    }


}
