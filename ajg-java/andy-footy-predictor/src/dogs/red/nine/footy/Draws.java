package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Draws extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("Draws");

	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 10;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=0;
		int awayTeamToScoreRating=0;

     	homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();

		if (addPrediction(fd.getDate())) {
			if ((diffRating(homeTeamToScoreRating, awayTeamToScoreRating) < MAGIC_THRESHOLD)) {
				FixtureDraw fdr = new FixtureDraw(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(fdr);
			}
		}
	}
	
	
	private int diffRating(int homeTeamToScoreRating, int awayTeamToScoreRating) {
		if (homeTeamToScoreRating == awayTeamToScoreRating) {
			return 0;
		} else if (homeTeamToScoreRating > awayTeamToScoreRating) {
			return (homeTeamToScoreRating - awayTeamToScoreRating);
		} else {
			return (awayTeamToScoreRating - homeTeamToScoreRating);
		}
	}


	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Draws");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureDraw fdr = (FixtureDraw) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();
			
			logger.info(fixture.fixturePrint() + "  [** " + (fdr.getHomeTeamToScoreRating()+fdr.getAwayTeamToScoreRating()) + " **] --- (H:"
			        + fdr.getHomeTeamToScoreRating() + " A:" + fdr.getAwayTeamToScoreRating() + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}



}
