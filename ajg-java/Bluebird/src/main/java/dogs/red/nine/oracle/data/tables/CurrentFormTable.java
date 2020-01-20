package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CurrentFormTable extends Table {
    private static final Logger logger = LogManager.getLogger("CurrentFormTable");
    protected Map<String, Integer> formGamesAdded = new LinkedHashMap<String, Integer>();


    protected CurrentFormTable(Division division, SortedSet<String> teams) {
        super("Form Table", division, teams);
        for (String team : teams) {
            table.put(team, new CurrentFormTableEntry(team));
            formGamesAdded.put(team, 0);
        }
    }

    public void generateTable(List<MatchData> matches) {

        ListIterator<MatchData> matchIterator = matches.listIterator(matches.size());

        while (matchIterator.hasPrevious()) {
            MatchData currMatch = matchIterator.previous();

            checkAndAddResult(currMatch.getHomeTeam(), currMatch);
            checkAndAddResult(currMatch.getAwayTeam(), currMatch);
        }
        table = sortTable();
    }

    private void checkAndAddResult(final String team, final MatchData match) {
        if (formGamesAdded.get(team) < AppConstants.CURRENT_FORM_GAMES) {
            table.get(team).addResult(match);
            // increment games added for this team
            formGamesAdded.merge(team, 1, Integer::sum);
        }
    }

}
