package dogs.red.nine.footy;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixtureOver2p5Goals extends FixtureData implements Comparable<FixtureOver2p5Goals> {

	private static final Logger logger = LogManager.getLogger("FixtureOver2p5Goals ");

	private final int matchScoreRating;

	public FixtureOver2p5Goals(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		
		int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		
		matchScoreRating = homeTeamScoreRating  + awayTeamScoreRating;  
	}

	@Override
	public int compareTo(FixtureOver2p5Goals other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		return (other.getMatchScoreRating() - this.getMatchScoreRating());
	}

	public int getMatchScoreRating() {
		return matchScoreRating;
	}

}
