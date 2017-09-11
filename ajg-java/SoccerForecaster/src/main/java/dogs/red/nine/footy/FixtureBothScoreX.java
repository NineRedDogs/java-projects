package dogs.red.nine.footy;

import java.util.Date;

public class FixtureBothScoreX extends FixtureData implements Comparable<FixtureBothScoreX> {

	private final int totalMatchScoreRating;

	public FixtureBothScoreX(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		
		int homeTeamScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		int awayTeamScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();

		totalMatchScoreRating = homeTeamScoreRating  + awayTeamScoreRating; 
	}

	@Override
	public int compareTo(FixtureBothScoreX other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		return (other.getMatchScoreRating() - this.getMatchScoreRating());
	}

	public int getMatchScoreRating() {
		return totalMatchScoreRating;
	}
}
