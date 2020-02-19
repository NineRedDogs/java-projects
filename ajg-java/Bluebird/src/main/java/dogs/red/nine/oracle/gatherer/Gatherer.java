package dogs.red.nine.oracle.gatherer;

import java.io.IOException;
import java.util.*;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.*;
import dogs.red.nine.oracle.data.tables.DivisionTableManager;
import dogs.red.nine.oracle.data.tables.TableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Gatherer {
	
	private static final Logger logger = LogManager.getLogger("Gatherer");

	private final List<Division> leaguesToProcess;

	private final TableManager tabMgr;

	private final List<FixtureData> fixtures;

	private final Map<Division, List<MatchData>> allMatches;


	public Gatherer() throws IOException {
		super();

		if (AppConstants.DEV_MODE) {
			//leaguesToProcess = AppConstants.EPL;
			//leaguesToProcess = AppConstants.ENG_TOP2;
			//leaguesToProcess = AppConstants.ENG_DIVISIONS;
			leaguesToProcess = AppConstants.UK_DIVISIONS;
		} else if (AppConstants.USE_ALL_LEAGUES) {
			leaguesToProcess = AppConstants.ALL_DIVISIONS;
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

		// use results to generates the tables for each match day through the season
		tabMgr = new TableManager(getLeaguesToProcess());
		tabMgr.generateTables(allMatches);
	}

	public List<Division> getLeaguesToProcess() {
		return leaguesToProcess;
	}

	public List<FixtureData> getFixtures() {
		return fixtures;
	}

	public TableManager getTableManager() {
		return tabMgr;
	}
}
