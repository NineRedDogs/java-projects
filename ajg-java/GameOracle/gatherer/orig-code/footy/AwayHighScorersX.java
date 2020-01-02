package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwayHighScorersX extends HighScorersForecastType {
	
	private static final Logger logger = LogManager.getLogger("AwayHighScorers");

	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 50;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int awayTeamToScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

		if ((extraRulesForInclusion(fd)) && (addPrediction(fd.getDate()))) {
			//logger.debug("(AwayHS) Checking : " + fd.fixturePrint() + " atts: " + awayTeamToScoreRating);

			if (awayTeamToScoreRating > MAGIC_THRESHOLD) {
				FixtureHighScorerX fhs = new FixtureHighScorerX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam, HomeOrAway.Away);
				//logger.debug("     (AwayHS) Adding: " + fd.fixturePrint() + " atts: " + fhs.getAwayTeamToScoreRating());
				fixtures.add(fhs);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;

		logger.info("---------------------------------------");
		logger.info("---   Away high scorers X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHighScorerX fhs = (FixtureHighScorerX) fixture;

			// only interested in away high scorers here
			if (fhs.isAwayHS()) {
				Team homeTeam = fixture.getHomeTeam();
				Team awayTeam = fixture.getAwayTeam();

				logger.info(fixture.fixturePrint(DisplayExtras.HighlightAwayTeam) + "  [** " + fhs.getAwayTeamToScoreRating() + " **]");
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
