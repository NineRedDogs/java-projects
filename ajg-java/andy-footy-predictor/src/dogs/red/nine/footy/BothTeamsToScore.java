package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BothTeamsToScore extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("BothTeamsToScore");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD_HOME_TO_SCORE = 66;
		final int MAGIC_THRESHOLD_HOME_TO_CONCEDE = 66;
		final int MAGIC_THRESHOLD_AWAY_TO_SCORE = 66;
		final int MAGIC_THRESHOLD_AWAY_TO_CONCEDE = 66;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=0;
		int awayTeamToScoreRating=0;

		if ((homeTeam.getToScoreHomeRating() > MAGIC_THRESHOLD_HOME_TO_SCORE) && ((awayTeam.getToConcedeAwayRating() > MAGIC_THRESHOLD_AWAY_TO_CONCEDE))) {
			homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		}

		if ((awayTeam.getToScoreAwayRating() > MAGIC_THRESHOLD_AWAY_TO_SCORE) && ((homeTeam.getToConcedeHomeRating() > MAGIC_THRESHOLD_HOME_TO_CONCEDE))) {
			awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();
		}

		if (addPrediction(fd.getDate())) {
			if ((homeTeamToScoreRating > 0) && (awayTeamToScoreRating > 0)) {
				FixtureBothScore fbs = new FixtureBothScore(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(fbs);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Both teams to score");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureBothScore fbs = (FixtureBothScore) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();

			logger.info(fixture.fixturePrint() + "  [** " + (fbs.getHomeTeamToScoreRating()+fbs.getAwayTeamToScoreRating()) + " **] --- (H:"
			        + fbs.getHomeTeamToScoreRating() + " A:" + fbs.getAwayTeamToScoreRating() + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}


}
