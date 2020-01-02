package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeHighScorersX extends HighScorersForecastType {
	
	private static final Logger logger = LogManager.getLogger("HomeHighScorers");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 50;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();

		
		if ((extraRulesForInclusion(fd)) && (addPrediction(fd.getDate()))) {
			//logger.debug("(HomeHS) Checking : " + fd.fixturePrint() + " htts: " + homeTeamToScoreRating);

			if (homeTeamToScoreRating > MAGIC_THRESHOLD) {
				FixtureHighScorerX fhs = new FixtureHighScorerX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam, HomeOrAway.Home);
				fixtures.add(fhs);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		
		logger.info("---------------------------------------");
		logger.info("---   Home high scorers X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHighScorerX fhs = (FixtureHighScorerX) fixture;
			// only interested in home high scorers here
			if (fhs.isHomeHS()) {
				Team homeTeam = fixture.getHomeTeam();
				Team awayTeam = fixture.getAwayTeam();

				logger.info(fixture.fixturePrint(DisplayExtras.HighlightHomeTeam) + "  [** " + fhs.getHomeTeamToScoreRating() + " **]");
				if (Forecaster.SHOW_DETAILED_STATS) {
					logger.info("     " + homeTeam.getHomeStats());
					logger.info("     " + awayTeam.getAwayStats() + "\n");
				}

				if (count++ >= numFixturesToDisplay) break;
			}
		}
		logger.info("---------------------------------------");
	}


	
	

}
