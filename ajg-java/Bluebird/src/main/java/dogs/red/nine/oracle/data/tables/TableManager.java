package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TableManager {
    private static final Logger logger = LogManager.getLogger("TableManager");

    private final Map<Division, DivisionTableManager> allTables = new HashMap<Division, DivisionTableManager>();
    private final Map<Division, SortedSet<String>> listOfTeams = new HashMap<Division, SortedSet<String>>();

    private final List<Division> supportedDivisions;
    private final Config config;

    public TableManager(List<Division> supportedDivisions, Config cfg) {
        this.supportedDivisions = supportedDivisions;
        this.config = cfg;
    }

    private Division whatDivision(final String teamName) {
        for (Division div : getLeaguesToProcess()) {
            if (listOfTeams.get(div).contains(teamName)) {
                return div;
            }
        }
        logger.error("tried to find what division '" + teamName + "' play in, but failed to find a division, expect an NPE ....");
        return null;
    }

    private void generateTeamLists(Map<Division, List<MatchData>> allMatches) {
        for (Division division : getLeaguesToProcess()) {
            SortedSet divTeams = new TreeSet<String>();

            for (MatchData match : allMatches.get(division)) {
                divTeams.add(match.getHomeTeam());
                divTeams.add(match.getAwayTeam());
            }
            listOfTeams.put(division, divTeams);
        }
        //displayTeamLists();
    }

    public List<Division> getLeaguesToProcess() {
        return supportedDivisions;
    }

    public Config getConfig() {
        return config;
    }

    private void displayTeamLists() {
        for (Division division : getLeaguesToProcess()) {
            logger.debug("--------------------------------------------");
            logger.debug("Teams for " + division);

            for (String team : listOfTeams.get(division)) {
                logger.debug("   " + team);
            }
            logger.debug("  ");
        }
    }

    public void generateTables(Map<Division, List<MatchData>> allMatches) {
        generateTeamLists(allMatches);
        for (Division division : getLeaguesToProcess()) {
            final DivisionTableManager tabGen = new DivisionTableManager(getConfig());

            tabGen.generateTables(division, allMatches.get(division), listOfTeams.get(division));
            allTables.put(division, tabGen);

            tabGen.displayCurrentTables();
        }
    }


    public TableEntry getHomeFormData(String teamName) {
        return allTables.get(whatDivision(teamName)).getHomeFormData(teamName);
    }

    public TableEntry getAwayFormData(String teamName) {
        return allTables.get(whatDivision(teamName)).getAwayFormData(teamName);
    }

    public TableEntry getFormData(String teamName) {
        return allTables.get(whatDivision(teamName)).getFormData(teamName);
    }

    public TableEntry getHomeSeasonData(String teamName) {
        return allTables.get(whatDivision(teamName)).getHomeSeasonData(teamName);
    }

    public TableEntry getAwaySeasonData(String teamName) {
        return allTables.get(whatDivision(teamName)).getAwaySeasonData(teamName);
    }
}
