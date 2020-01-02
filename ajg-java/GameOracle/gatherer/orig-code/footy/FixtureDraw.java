package dogs.red.nine.footy;

import java.util.Date;

public class FixtureDraw extends FixtureData implements Comparable<FixtureDraw> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;

	public FixtureDraw(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();
	}

	@Override
	public int compareTo(FixtureDraw other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		
		int thisMatchRating = diffRating(this.getHomeTeamToScoreRating(), this.getAwayTeamToScoreRating());
		int otherMatchRating = diffRating(other.getHomeTeamToScoreRating(), other.getAwayTeamToScoreRating());
		
		return (otherMatchRating - thisMatchRating);
	}

	public int getHomeTeamToScoreRating() {
		return homeTeamToScoreRating;
	}

	public int getAwayTeamToScoreRating() {
		return awayTeamToScoreRating;
	}
	
	
	private int diffRating(int homeTeamToScoreRating, int awayTeamToScoreRating) {
		if (homeTeamToScoreRating == awayTeamToScoreRating) {
			return 0;
		} else if (homeTeamToScoreRating > awayTeamToScoreRating) {
			return (homeTeamToScoreRating - awayTeamToScoreRating);
		} else {
			return (awayTeamToScoreRating - homeTeamToScoreRating);
		}
	}


}
