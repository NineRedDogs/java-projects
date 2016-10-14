package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwayHighScorers extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("AwayHighScorers");

	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 300;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int awayTeamToScoreRating=0;
     	awayTeamToScoreRating = awayTeam.getToScoreHomeRating() + homeTeam.getToConcedeAwayRating();

		if (awayTeamToScoreRating > MAGIC_THRESHOLD) {
			FixtureHighScorer fhs = new FixtureHighScorer(fd.getDivision(), fd.getDate(), homeTeam, awayTeam, HomeOrAway.Away);
			fixtures.add(fhs);
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;

		logger.info("---------------------------------------");
		logger.info("---   Away high scorers");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHighScorer fhs = (FixtureHighScorer) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();

			logger.info(fixture.fixturePrint() + "  [** " + (fhs.getHomeTeamToScoreRating()+fhs.getAwayTeamToScoreRating()) + " **] --- (H:"
			        + fhs.getHomeTeamToScoreRating() + " A:" + fhs.getAwayTeamToScoreRating() + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}




}
