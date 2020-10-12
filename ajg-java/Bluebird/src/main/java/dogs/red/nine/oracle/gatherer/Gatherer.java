package dogs.red.nine.oracle.gatherer;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.Config;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.data.tables.TableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Gatherer {
	
	private static final Logger logger = LogManager.getLogger("Gatherer");

	private final List<Division> leaguesToProcess;

	private final TableManager tabMgr;

	private final List<FixtureData> fixtures;

	private final Config config;

	private final Map<Division, List<MatchData>> allMatches;



	public Gatherer(Config cfg) throws IOException {
		super();
		this.config = cfg;
		List<Division> initialListOfLeagues = setLeagues(cfg.getLeaguesToUse());

		/** go get the results for the chosen divisions */
		GetResults gResults = new GetResults(initialListOfLeagues, config.getSeason());
		allMatches = gResults.getResultsFromDataUrls();
		this.leaguesToProcess = gResults.getActiveLeagues();

		/** go get the fixtures for the chosen divisions */
		GetFixtures gFixtures = new GetFixtures(getLeaguesToProcess());
		fixtures = gFixtures.getFixtures();

		// use results to generates the tables for each match day through the season
		tabMgr = new TableManager(getLeaguesToProcess(), config);
		tabMgr.generateTables(allMatches);
	}


	private List<Division> setLeagues(String configLeaguesToUse) {

		List<Division> leagues;

		switch(configLeaguesToUse) {

			case "UK": {
				leagues = AppConstants.UK_DIVISIONS;
				break;
			}

			case "EU": {
				leagues = AppConstants.EURO_DIVISIONS;
				break;
			}

			case "ELITE": {
				leagues = AppConstants.ELITE;
				break;
			}

			case "EU_ELITE": {
				leagues = AppConstants.EURO_ELITE;
				break;
			}

			case "EPL": {
				leagues = AppConstants.EPL;
				break;
			}

			case "ENG": {
				leagues = AppConstants.ENG_DIVISIONS;
				break;
			}

			case "SCO": {
				leagues = AppConstants.SCOT_DIVISIONS;
				break;
			}

			case "GER": {
				leagues = AppConstants.GER;
				break;
			}

			default: {
				leagues = AppConstants.ALL_DIVISIONS;
				break;
			}
		}
		return leagues;
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
