package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BothTeamsToScoreX extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("BothTeamsToScore");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD_HOME_TO_SCORE = 120;
		final int MAGIC_THRESHOLD_AWAY_TO_SCORE = 100;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		
		if (addPrediction(fd.getDate())) {
			if ((homeTeamScoreRating > MAGIC_THRESHOLD_HOME_TO_SCORE) && (awayTeamScoreRating > MAGIC_THRESHOLD_AWAY_TO_SCORE)) {
				FixtureBothScoreX fbs = new FixtureBothScoreX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(fbs);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Both teams to score X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureBothScoreX fbs = (FixtureBothScoreX) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();
			int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
			int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

			logger.info(fixture.fixturePrint() + "  [** " + (homeTeamScoreRating+awayTeamScoreRating) + " **] --- (H:"
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
