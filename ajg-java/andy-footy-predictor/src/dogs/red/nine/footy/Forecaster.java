package dogs.red.nine.footy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Forecaster {
	
    private static final Logger logger = LogManager.getLogger("Forecaster");

    private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(
			Division.England_Premier_League, 
			Division.England_Championship, 
			Division.England_League_1,
			Division.England_League_2,
			Division.England_Conference,
			//Division.Germany_Bundesliga_1 ,
			//Division.Spain_La_Liga ,
			//Division.Italy_Serie_A ,
			//Division.France_Ligue_1 ,
			//Division.Holland_Eridivisie ,
			//Division.Belgium_Juliper_1 ,
			//Division.Portugal_Primera ,
			Division.Scotland_Premier_League ,
			Division.Scotland_Championship ,
			Division.Scotland_Div_1 ,
			Division.Scotland_Div_2);

	public static final boolean SHOW_DETAILED_STATS = false;

	// private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.England_Premier_League); used when developing ....
	//private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.England_Championship); 
	//private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.Scotland_Championship); 

	private BothTeamsToScore bothTeamsScore;
	private HomeWins homeWins;
	private AwayWins awayWins;
	private Draws draws;
	private HomeHighScorers homeHighScorers;
	private AwayHighScorers awayHighScorers;


	/**
	 */
	public Forecaster() {
		super();
		this.bothTeamsScore = new BothTeamsToScore();
		this.homeWins = new HomeWins();
		this.awayWins = new AwayWins();
		this.draws = new Draws();
		this.homeHighScorers = new HomeHighScorers();
		this.awayHighScorers = new AwayHighScorers();
	}


	private Teams getTeams(List<FixtureData> fixtures) {
        logger.entry("x");
		Teams teams = new Teams();

		for (FixtureData fixture : fixtures) {
			teams.updateTeams(fixture.getHomeTeam().getName());
			teams.updateTeams(fixture.getAwayTeam().getName());
		}
		return teams;
	}


	private void testBothTeamsToScore(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		bothTeamsScore.process(fixtureAnalysis);
	}
	
	private void testHomeWin(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		homeWins.process(fixtureAnalysis);
	}
	
	private void testAwayWin(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		awayWins.process(fixtureAnalysis);
	}
	
	private void testDraw(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		draws.process(fixtureAnalysis);
	}

	private void testHomeHighScorers(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		homeHighScorers.process(fixtureAnalysis);
	}
	
	private void testAwayHighScorers(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		awayHighScorers.process(fixtureAnalysis);
	}

	private void displayResults() {
		bothTeamsScore.display(24);
		homeWins.display(20);
		awayWins.display(10);
		//draws.display(10);
		homeHighScorers.display(16);
		awayHighScorers.display(16);
	}



	public static void main(String[] args) throws IOException, ParseException {

		Forecaster f = new Forecaster();

		GetFixtures gf = new GetFixtures(SUPPORTED_DIVISIONS);
		List<FixtureData> fixtures = gf.getFixtures();

		Teams teams = f.getTeams(fixtures); 
		//System.out.println("Teams: " + teams.toString());

		GetResults gr = new GetResults(SUPPORTED_DIVISIONS);
		gr.getResultsFromDataUrls(teams);
		
		// get todays date
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));


		for (FixtureData fixture : fixtures) {
			
			// only check if fixture has not yet happened
			if (!fixture.getDate().before(todayDate)) {

				Team homeTeam = teams.getTeam(fixture.getHomeTeam().getName());
				Team awayTeam = teams.getTeam(fixture.getAwayTeam().getName());

				// both teams to score 
				f.testBothTeamsToScore(fixture, homeTeam, awayTeam);

				// home win
				f.testHomeWin(fixture, homeTeam, awayTeam);

				// away win
				f.testAwayWin(fixture, homeTeam, awayTeam);

				// draw
				f.testDraw(fixture, homeTeam, awayTeam);

				// high scorers (home)
				f.testHomeHighScorers(fixture, homeTeam, awayTeam);

				// high scorers away
				f.testAwayHighScorers(fixture, homeTeam, awayTeam);
			}
		}
		// lets have a look at the results
		f.displayResults();
	}



}
