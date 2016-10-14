package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeWins extends ForecastType {
	private static final Logger logger = LogManager.getLogger("HomeWins");

	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 66;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=0;
		int awayTeamToScoreRating=0;

     	homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();

		if ((homeTeamToScoreRating - awayTeamToScoreRating) > MAGIC_THRESHOLD) {
			FixtureHomeWin fhw = new FixtureHomeWin(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
			fixtures.add(fhw);
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Home wins ");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHomeWin fhw = (FixtureHomeWin) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();
			
			logger.info(fixture.fixturePrint() + "  [** " + (fhw.getHomeTeamToScoreRating()+fhw.getAwayTeamToScoreRating()) + " **] --- (H:"
			        + fhw.getHomeTeamToScoreRating() + " A:" + fhw.getAwayTeamToScoreRating() + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}



}
