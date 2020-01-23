package dogs.red.nine.oracle.gatherer;

import java.io.IOException;
import java.util.*;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.*;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Gatherer {
	
	private static final Logger logger = LogManager.getLogger("Gatherer");

	private final List<Division> leaguesToProcess;

	private final TableGenerator tabGen = new TableGenerator();

	private final List<FixtureData> fixtures;

	private final Map<Division, List<MatchData>> allMatches;

	private final Map<Division, SortedSet<String>> listOfTeams = new HashMap<Division, SortedSet<String>>();

	public Gatherer() throws IOException {
		super();

		if (AppConstants.DEV_MODE) {
			//leaguesToProcess = AppConstants.EPL;
			leaguesToProcess = AppConstants.ENG_TOP2;
			//leaguesToProcess = AppConstants.ENG_DIVISIONS;
		} else if (AppConstants.USE_UK_LEAGUES) {
			leaguesToProcess = AppConstants.UK_DIVISIONS;
		} else {
			leaguesToProcess = AppConstants.EURO_DIVISIONS;
		}

		/** go get the fixtures for the chosen divisions */
		GetFixtures gFixtures = new GetFixtures(getLeaguesToProcess());
		fixtures = gFixtures.getFixtures();

		/** go get the results for the chosen divisions */
		GetResults gResults = new GetResults(getLeaguesToProcess());
		allMatches = gResults.getResultsFromDataUrls();

		/** generate list of teams per division */
		generateTeamLists();

		// use results to generates the tables for each match day through the season
		generateTables();
	}

	public List<Division> getLeaguesToProcess() {
		return leaguesToProcess;
	}

	public List<FixtureData> getFixtures() {
		return fixtures;
	}


	private void generateTeamLists() {
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

	private void generateTables() {
		for (Division division : getLeaguesToProcess()) {
			tabGen.generateTables(division, allMatches.get(division), listOfTeams.get(division));
			//tabGen.displayCurrentTables();
		}
	}

	public TableGenerator getTableGenerator() {
		return tabGen;
	}
}
