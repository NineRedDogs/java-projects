package dogs.red.nine.footy;

import java.util.Date;

public class FixtureBothScore extends FixtureData implements Comparable<FixtureBothScore> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;

	public FixtureBothScore(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();
	}

	@Override
	public int compareTo(FixtureBothScore other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		
		int thisMatchRating = this.getHomeTeamToScoreRating()+this.getAwayTeamToScoreRating();
		int otherMatchRating = other.getHomeTeamToScoreRating()+other.getAwayTeamToScoreRating();

		return (otherMatchRating - thisMatchRating);
	}

	public int getHomeTeamToScoreRating() {
		return homeTeamToScoreRating;
	}

	public int getAwayTeamToScoreRating() {
		return awayTeamToScoreRating;
	}

}
