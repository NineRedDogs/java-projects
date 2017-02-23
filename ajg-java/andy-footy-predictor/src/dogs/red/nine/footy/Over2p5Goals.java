package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Over2p5Goals extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("BothTeamsToScore");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD_GOALS = 250;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		
		int totalGoalsScoredRating = homeTeamScoreRating  + awayTeamScoreRating;  
		
		if (addPrediction(fd.getDate())) {
			if (totalGoalsScoredRating > MAGIC_THRESHOLD_GOALS) {
				FixtureOver2p5Goals fbs = new FixtureOver2p5Goals(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(fbs);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Over 2.5 goals ");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureOver2p5Goals fbs = (FixtureOver2p5Goals) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();
			int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
			int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
			
			int totalGoalsScoredRating = homeTeamScoreRating  + awayTeamScoreRating;  

			logger.info(fixture.fixturePrint() + "  [** " + totalGoalsScoredRating + " **] --- (H:"
			        + homeTeamScoreRating + " A:" + awayTeamScoreRating + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}


}
