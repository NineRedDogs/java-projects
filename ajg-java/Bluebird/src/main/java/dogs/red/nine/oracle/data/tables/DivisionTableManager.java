package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class DivisionTableManager {

    private static final Logger logger = LogManager.getLogger("DivisionTableManager");
    private final SortedMap<Date, Table> fullTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> fullHomeTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> fullAwayTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> formTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> formHomeTables = new ConcurrentSkipListMap<Date, Table>();
    private final SortedMap<Date, Table> formAwayTables = new ConcurrentSkipListMap<Date, Table>();

    public void generateTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {
        generateFullSeasonTables(division, matchData, teams);
        generateFormTables(division, matchData, teams);
    }


    public void displayCurrentTables() {
        fullTables.get(fullTables.lastKey()).displayTable("Current League Table");
        fullHomeTables.get(fullHomeTables.lastKey()).displayTable("Current Home League Table");
        fullAwayTables.get(fullAwayTables.lastKey()).displayTable("Current Away League Table");
        formTables.get(formTables.lastKey()).displayTable("Current Form Table");
        formHomeTables.get(formHomeTables.lastKey()).displayTable("Current Form Home Table");
        formAwayTables.get(formAwayTables.lastKey()).displayTable("Current Form Away Table");
    }

    private void generateFullSeasonTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        Date currDate = matchData.get(0).getDate();
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

    private void genFullSeasonTable(Division division, SortedSet<String> teams, List<MatchData> tableMatches, Date currDate) {
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
        //fullSeasonAwayTable.displayTable(currDate.toString());

        FullSeasonMeritTable fullSeasonMeritTable = new FullSeasonMeritTable(division, teams);
        fullSeasonMeritTable.generateTable(tableMatches);
        fullTables.put(currDate, fullSeasonMeritTable);
        fullSeasonMeritTable.displayTable(currDate.toString());
    }



    private void generateFormTables(Division division, List<MatchData> matchData, SortedSet<String> teams) {

        // generate all the full season tables - one for each date ...
        Date currDate = matchData.get(0).getDate();
        List<MatchData> tableMatches = new ArrayList<MatchData>();

        // create a new matchData structure
        for (MatchData match : matchData) {
            if (currDate.compareTo(match.getDate()) < 0) {
                // current match has a new date, so generate table for all matches so far
                genFormTable(division, teams, tableMatches, currDate);

                // move curr date on
                currDate = match.getDate();
            }
            tableMatches.add(match);
        }

        // record final table
        genFullSeasonTable(division, teams, tableMatches, currDate);

    }

    private void genFormTable(Division division, SortedSet<String> teams, List<MatchData> tableMatches, Date currDate) {
        CurrentFormTable currentFormTable = new CurrentFormTable(division, teams);
        currentFormTable.generateTable(tableMatches);
        formTables.put(currDate, currentFormTable);
        //currentFormTable.displayTable(currDate.toString());

        CurrentFormHomeTable currentFormHomeTable = new CurrentFormHomeTable(division, teams);
        currentFormHomeTable.generateTable(tableMatches);
        formHomeTables.put(currDate, currentFormHomeTable);
        //currentFormHomeTable.displayTable(currDate.toString());

        CurrentFormAwayTable currentFormAwayTable = new CurrentFormAwayTable(division, teams);
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
