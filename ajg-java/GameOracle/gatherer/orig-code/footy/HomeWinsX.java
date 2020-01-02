package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeWinsX extends ForecastType {
	private static final Logger logger = LogManager.getLogger("HomeWins");

	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 100;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamToScoreRating=awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

		if (addPrediction(fd.getDate())) {
			if ((homeTeamToScoreRating - awayTeamToScoreRating) > MAGIC_THRESHOLD) {
				FixtureHomeWinX fhw = new FixtureHomeWinX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(fhw);
			}
		}
	}
	
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Home wins X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureHomeWinX faw = (FixtureHomeWinX) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();

			int homeTeamToScoreRating=homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
			int awayTeamToScoreRating=awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
			int homeTeamWinRating = (homeTeamToScoreRating - awayTeamToScoreRating);
			
			logger.info(fixture.fixturePrint(DisplayExtras.HighlightHomeTeam) + "  [** " + homeTeamWinRating + " **] --- (H:"
			        + homeTeamToScoreRating + " A:" + awayTeamToScoreRating + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}



}
