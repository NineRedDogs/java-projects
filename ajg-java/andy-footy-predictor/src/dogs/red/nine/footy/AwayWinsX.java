package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwayWinsX extends ForecastType {
	
	private static final Logger logger = LogManager.getLogger("AwayWins");


	public void process(FixtureData fd) {
		
		final int MAGIC_THRESHOLD = 5;

		Team homeTeam = fd.getHomeTeam();
		Team awayTeam = fd.getAwayTeam();

		int homeTeamToScoreRating=homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamToScoreRating=awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

		if (addPrediction(fd.getDate())) {
			if ((awayTeamToScoreRating - homeTeamToScoreRating) > MAGIC_THRESHOLD) {
				FixtureAwayWinX faw = new FixtureAwayWinX(fd.getDivision(), fd.getDate(), homeTeam, awayTeam);
				fixtures.add(faw);
			}
		}
	}
	
	public void display(int numFixturesToDisplay) {
		int count=1;
		logger.info("---------------------------------------");
		logger.info("---   Away wins X");
		logger.info("-------------------------");
		for (FixtureData fixture : fixtures) {

			FixtureAwayWinX faw = (FixtureAwayWinX) fixture;
			Team homeTeam = fixture.getHomeTeam();
			Team awayTeam = fixture.getAwayTeam();

			int homeTeamToScoreRating=homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
			int awayTeamToScoreRating=awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
			int awayTeamWinRating = (awayTeamToScoreRating - homeTeamToScoreRating);
			
			logger.info(fixture.fixturePrint(DisplayExtras.HighlightAwayTeam) + "  [** " + awayTeamWinRating + " **] --- (A:"
			        + awayTeamToScoreRating + " H:" + homeTeamToScoreRating + ")");
			if (Forecaster.SHOW_DETAILED_STATS) {
				logger.info("     " + homeTeam.getHomeStats());
				logger.info("     " + awayTeam.getAwayStats() + "\n");
			}

			if (count++ >= numFixturesToDisplay) break;
		}
		logger.info("---------------------------------------");
	}



}
