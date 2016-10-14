package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwayWins extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("AwayWins");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 66;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=0;
		int awayTeamToScoreRating=0;

     	homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();

		if ((awayTeamToScoreRating - homeTeamToScoreRating) > MAGIC_THRESHOLD) {
			FixtureAwayWin faw = new FixtureAwayWin(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
			fixtures.add(faw);
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Away wins ");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureAwayWin faw = (FixtureAwayWin) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();

			logger.info(fixture.fixturePrint() + "  [** " + (faw.getHomeTeamToScoreRating()+faw.getAwayTeamToScoreRating()) + " **] --- (H:"
			        + faw.getHomeTeamToScoreRating() + " A:" + faw.getAwayTeamToScoreRating() + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}



}
