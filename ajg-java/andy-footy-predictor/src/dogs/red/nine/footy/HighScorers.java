package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HighScorers extends HighScorersForecastType {

	private static final Logger logger = LogManager.getLogger("HighScorers");

	public void process(FixtureData fd) {

		final int MAGIC_THRESHOLD = 50;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamToScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

		if ((extraRulesForInclusion(fd)) && (addPrediction(fd.getDate()))) {
			//logger.debug("(AllHS) Checking : " + fd.fixturePrint() + " htts: " + homeTeamToScoreRating + "   atts: " + awayTeamToScoreRating);
			if (homeTeamToScoreRating > MAGIC_THRESHOLD) {
				FixtureHighScorerX hhs = new FixtureHighScorerX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam, HomeOrAway.Home);
				fixtures.add(hhs);
				//logger.debug("   --- Adding (home): " + fd.fixturePrint() + " htts: " + homeTeamToScoreRating);
			}
			
			if (awayTeamToScoreRating > MAGIC_THRESHOLD) {
				FixtureHighScorerX ahs = new FixtureHighScorerX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam, HomeOrAway.Away);
				fixtures.add(ahs);
				//logger.debug("   --- Adding (away): " + fd.fixturePrint() + " atts: " + awayTeamToScoreRating);
			}
		}
	}


	public void display(int numFixturesToDisplay) {
		int count=1;

		logger.info("---------------------------------------");
		logger.info("---   high scorers X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHighScorerX fhs = (FixtureHighScorerX) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();
			
			if (fhs.isHomeHS()) {
				logger.info(fixture.fixturePrint(DisplayExtras.HighlightHomeTeam) + "  [** " + fhs.getHomeTeamToScoreRating() + " **]");
			} else {
				logger.info(fixture.fixturePrint(DisplayExtras.HighlightAwayTeam) + "  [** " + fhs.getAwayTeamToScoreRating() + " **]");
			}
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}

}
