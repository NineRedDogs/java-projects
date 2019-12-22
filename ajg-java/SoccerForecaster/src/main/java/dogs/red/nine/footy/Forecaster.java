package dogs.red.nine.footy;

import java.io.File;
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
	
	private static final String DATA_DIR_NAME = "data";

	public static final File CWD = new File(System.getProperty("user.dir"));
	public static final File DATA_DIR = new File(CWD, DATA_DIR_NAME);

    
	// set this to use a local sample fixture file, instead of trying to retrieve from remote site 
	public static final boolean DEV_MODE = false;
	public static final String SAMPLE_FIXTURE_DEV_MODE_FILE="/home/agrahame/Dropbox/java-projects/java-projects/ajg-java/andy-footy-predictor/data/sample_fixtures_used_for_dev_mode.csv";

	// use this flag to use this weeks fixtures that have already been played, useful if hanging around for Friday in order to get some useful runs....
	// this flag means we'll go off and get the fixture data from remote website - instead of using the local hard-coded fixture file used when DEV_MODE flag is set
	// note: if this is true, then set DEV_MODE to false, otherwise local fixtures sample file will be used ...
	public static final boolean DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES = false;

	
	/** Set this to only list predictions for todays games */
	public static final boolean ONLY_TODAYS_GAMES = true;
	//public static final boolean ONLY_TODAYS_GAMES = false;
	
	/** Set to true if use UK divisions, false to use EURO leagues */
	public static final boolean USE_UK_LEAGUES = true;
	//public static final boolean USE_UK_LEAGUES = false;

    private static final List<Division> UK_DIVISIONS = Arrays.asList(
			Division.England_Premier_League, 
			Division.England_Championship, 
			Division.England_League_1,
			Division.England_League_2,
			Division.England_Conference,
			Division.Scotland_Premier_League ,
			Division.Scotland_Championship ,
			Division.Scotland_Div_1 ,
			Division.Scotland_Div_2);

    private static final List<Division> EURO_DIVISIONS = Arrays.asList(
			Division.Germany_Bundesliga_1 ,
			Division.Spain_La_Liga ,
			Division.Italy_Serie_A ,
			Division.France_Ligue_1 ,
			Division.Holland_Eridivisie ,
			Division.Belgium_Juliper_1 ,
			Division.Portugal_Primera );

    
	public static final boolean SHOW_DETAILED_STATS = false;


	// private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.England_Premier_League); used when developing ....
	//private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.England_Championship); 
	//private static final List<Division> SUPPORTED_DIVISIONS = Arrays.asList(Division.Scotland_Championship); 

	private final List<Division> leaguesToProcess;
	private BothTeamsToScore bothTeamsScore;
	private BothTeamsToScoreX bothTeamsScoreX;
	private Over2p5Goals over2p5Goals;
	private HomeWins homeWins;
	private AwayWins awayWins;
	private HomeWinsX homeWinsX;
	private AwayWinsX awayWinsX;
	private Draws draws;
	private HomeHighScorers homeHighScorers;
	private AwayHighScorers awayHighScorers;
	private HomeHighScorersX homeHighScorersX;
	private AwayHighScorersX awayHighScorersX;
	private HighScorers highScorers;


	/**
	 */
	public Forecaster() {
		super();
		this.bothTeamsScore = new BothTeamsToScore();
		this.bothTeamsScoreX = new BothTeamsToScoreX();
		this.over2p5Goals = new Over2p5Goals();
		this.homeWins = new HomeWins();
		this.awayWins = new AwayWins();
		this.homeWinsX = new HomeWinsX();
		this.awayWinsX = new AwayWinsX();
		this.draws = new Draws();
		this.homeHighScorers = new HomeHighScorers();
		this.awayHighScorers = new AwayHighScorers();
		this.homeHighScorersX = new HomeHighScorersX();
		this.awayHighScorersX = new AwayHighScorersX();
		this.highScorers = new HighScorers();
		if (USE_UK_LEAGUES) {
			leaguesToProcess = UK_DIVISIONS;
		} else {
			leaguesToProcess = EURO_DIVISIONS;
		}
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
	
	private void testBothTeamsToScoreX(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		bothTeamsScoreX.process(fixtureAnalysis);
	}
	
	private void testOver2p5Goals(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		over2p5Goals.process(fixtureAnalysis);
	}
	
	private void testHomeWin(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		homeWins.process(fixtureAnalysis);
	}

	private void testHomeWinX(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		homeWinsX.process(fixtureAnalysis);
	}
	
	private void testAwayWin(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		awayWins.process(fixtureAnalysis);
	}
	
	private void testAwayWinX(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		awayWinsX.process(fixtureAnalysis);
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

	private void testHomeHighScorersX(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		homeHighScorersX.process(fixtureAnalysis);
	}
	
	private void testAwayHighScorersX(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		awayHighScorersX.process(fixtureAnalysis);
	}

	private void testHighScorers(FixtureData fixture, Team homeTeam, Team awayTeam) {
		FixtureData fixtureAnalysis = new FixtureData(fixture.getDivision(), fixture.getDate(), homeTeam, awayTeam);
		highScorers.process(fixtureAnalysis);
	}
	


	private void displayResults() {
		//bothTeamsScore.display(24);
		bothTeamsScoreX.display(30);
		over2p5Goals.display(30);
		//homeWins.display(10);
		homeWinsX.display(10);
		//awayWins.display(8);
		awayWinsX.display(6);
		//draws.display(10);
		//homeHighScorers.display(10);
		//awayHighScorers.display(10);
		//homeHighScorersX.display(20);
		//awayHighScorersX.display(12);
		
		highScorers.display(30);
	}
	
	public List<Division> getLeaguesToProcess() {
		return leaguesToProcess;
	}


	public static void main(String[] args) throws IOException, ParseException {
		Forecaster f = new Forecaster();

		GetFixtures gf = new GetFixtures(f.getLeaguesToProcess());
		List<FixtureData> fixtures = gf.getFixtures();

		Teams teams = f.getTeams(fixtures); 
		//System.out.println("Teams: " + teams.toString());

		GetResults gr = new GetResults(f.getLeaguesToProcess());
		gr.getResultsFromDataUrls(teams);
		
		// get todays date
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Date todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));


		for (FixtureData fixture : fixtures) {
			
			boolean processFixture = (!fixture.getDate().before(todayDate));
			
			// only check if fixture has not yet happened
			if (DEV_MODE || DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES || processFixture) {
				
				Team homeTeam = teams.getTeam(fixture.getHomeTeam().getName());
				Team awayTeam = teams.getTeam(fixture.getAwayTeam().getName());

				// both teams to score 
				f.testBothTeamsToScore(fixture, homeTeam, awayTeam);

				// both teams to score X 
				f.testBothTeamsToScoreX(fixture, homeTeam, awayTeam);

				// over 2.5 goals
				f.testOver2p5Goals(fixture, homeTeam, awayTeam);

				// home win
				f.testHomeWin(fixture, homeTeam, awayTeam);

				// home win X
				f.testHomeWinX(fixture, homeTeam, awayTeam);

				// away win
				f.testAwayWin(fixture, homeTeam, awayTeam);

				// away win X
				f.testAwayWinX(fixture, homeTeam, awayTeam);

				// draw
				f.testDraw(fixture, homeTeam, awayTeam);

				// high scorers (home)
				f.testHomeHighScorers(fixture, homeTeam, awayTeam);

				// high scorersX (home)
				f.testHomeHighScorersX(fixture, homeTeam, awayTeam);

				// high scorers (away)
				f.testAwayHighScorers(fixture, homeTeam, awayTeam);
				
				// high scorersX (away)
				f.testAwayHighScorersX(fixture, homeTeam, awayTeam);
				
				// high scorers (all)
				f.testHighScorers(fixture, homeTeam, awayTeam);
			}
		}
		// lets have a look at the results
		f.displayResults();
		String predictionsFor="All fixtures";
		if (DEV_MODE) {
			predictionsFor="sample fixtures (DEV MODE enabled)";
		} else if (DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES) {
				predictionsFor="this weeks fixtures (DEV MODE)";
		} else if (ONLY_TODAYS_GAMES) {
			
			dateFormatter.applyPattern("EEEE d MMM yyyy");
			String myDate = dateFormatter.format(todayDate);
			
			predictionsFor="fixtures played on " + myDate;
		}
		logger.info("\nShowing predictions for " + predictionsFor + "\n\n");
	}





}
