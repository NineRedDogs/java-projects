package dogs.red.nine.oracle.gatherer;

import java.io.IOException;
import java.util.List;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.*;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

		if (AppConstants.DEV_MODE) {
			//leaguesToProcess = AppConstants.EPL;
			leaguesToProcess = AppConstants.ENG_TOP2;
			//leaguesToProcess = AppConstants.ENG_DIVISIONS;
		} else if (AppConstants.USE_UK_LEAGUES) {
			leaguesToProcess = AppConstants.UK_DIVISIONS;
		} else {
			leaguesToProcess = AppConstants.EURO_DIVISIONS;
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

	private TeamForecastData getTeamForecastData(String teamName, boolean isHomeTeam) {
		TeamForecastData teamForecastData = new TeamForecastData();
		if (isHomeTeam) {
			// 1a. add home form
			teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tableGenerator.getHomeFormData(teamName));
		} else {
			// 1b. add away form
			teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tableGenerator.getAwayFormData(teamName));
		}

		// 2. add general current form
		teamForecastData.addTeamForecastData(TeamForecastData.FORM_GENERAL, tableGenerator.getFormData(teamName));

		if (isHomeTeam) {
			// 3a. add season home form
			teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tableGenerator.getHomeSeasonData(teamName));
		} else {
			// 3b. add season away form
			teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tableGenerator.getAwaySeasonData(teamName));
		}
		return teamForecastData;
	}

	public FixtureForecastData getFixtureData(FixtureData fixture) {
		TeamForecastData htData = getTeamForecastData(fixture.getHomeTeam(), true);
		TeamForecastData atData = getTeamForecastData(fixture.getAwayTeam(), false);
		FixtureForecastData fData = new FixtureForecastData(htData, atData);

		return fData;
	}
}
