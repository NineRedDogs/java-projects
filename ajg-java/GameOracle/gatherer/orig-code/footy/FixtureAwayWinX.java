package dogs.red.nine.footy;

import java.util.Date;

public class FixtureAwayWinX extends FixtureData implements Comparable<FixtureAwayWinX> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;
	private final int awayTeamWinRating;

	public FixtureAwayWinX(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		
		awayTeamWinRating = (awayTeamToScoreRating - homeTeamToScoreRating);
	}

	@Override
	public int compareTo(FixtureAwayWinX other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		return (other.getAwayTeamWinRating() - this.awayTeamWinRating);
	}

	public int getAwayTeamWinRating() {
		return awayTeamWinRating;
	}

}
