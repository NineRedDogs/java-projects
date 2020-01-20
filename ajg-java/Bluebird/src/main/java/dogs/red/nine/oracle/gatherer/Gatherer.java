package dogs.red.nine.oracle.gatherer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.Teams;


public class Gatherer {
	
	private static final Logger logger = LogManager.getLogger("Gatherer");
	

	private final List<Division> leaguesToProcess;

	private final TableGenerator tableGenerator = new TableGenerator();

	private final GetFixtures gFixtures;
	private final GetResults gResults;

	private final List<FixtureData> fixtures;
	private final Teams teams;



	/**
         */
	public Gatherer() throws IOException {
		super();

		if (DEV_MODE) {
			leaguesToProcess = EPL;
			//leaguesToProcess = ENG_TOP2;
			//leaguesToProcess = ENG_DIVISIONS;
		} else if (USE_UK_LEAGUES) {
			leaguesToProcess = UK_DIVISIONS;
		} else {
			leaguesToProcess = EURO_DIVISIONS;
		}
		gFixtures = new GetFixtures(getLeaguesToProcess());
		fixtures = gFixtures.getFixtures();

		gResults = new GetResults(getLeaguesToProcess(), tableGenerator);
		teams = gResults.getResultsFromDataUrls();
	}

	public List<Division> getLeaguesToProcess() {
		return leaguesToProcess;
	}


	public List<FixtureData> getFixtures() {
		return fixtures;
	}
}
