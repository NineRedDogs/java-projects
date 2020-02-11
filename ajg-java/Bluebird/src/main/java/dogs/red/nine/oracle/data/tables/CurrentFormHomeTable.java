package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CurrentFormHomeTable extends HomeOrAwayTable {
    private static final Logger logger = LogManager.getLogger("CurrentFormHomeTable");
    protected Map<String, Integer> formGamesAdded = new LinkedHashMap<String, Integer>();

    protected CurrentFormHomeTable(Division division, SortedSet<String> teams, CurrentFormTable currentFormTable) {
        super("Form Home Table", division, teams, currentFormTable);
        for (String team : teams) {
            addEntry(team, new CurrentFormHomeTableEntry(team));
            formGamesAdded.put(team, 0);
        }
    }

    public void generateTable(List<MatchData> matches) {
        ListIterator<MatchData> matchIterator = matches.listIterator(matches.size());

        while (matchIterator.hasPrevious()) {
            MatchData currMatch = matchIterator.previous();

            checkAndAddResult(currMatch.getHomeTeam(), currMatch.getAwayTeam(), currMatch);
        }
        table = sortTable();
    }

    private void checkAndAddResult(final String us, final String them, final MatchData match) {
        if (formGamesAdded.get(us) < AppConstants.CURRENT_FORM_GAMES) {
            getEntry(us).addResult(match, getHomeAndAwayTable().getEntry(them));
            // increment games added for this team
            formGamesAdded.merge(us, 1, Integer::sum);
        }
    }

}
