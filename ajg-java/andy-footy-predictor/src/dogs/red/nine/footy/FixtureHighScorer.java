package dogs.red.nine.footy;

import java.util.Date;

public class FixtureHighScorer extends FixtureData implements Comparable<FixtureHighScorer> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;
	private final HomeOrAway homeOrAway;

	public FixtureHighScorer(Division division, Date date, Team homeTeam,
			Team awayTeam, HomeOrAway homeOrAway) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRating() + awayTeam.getToConcedeAwayRating();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRating() + homeTeam.getToConcedeAwayRating();
		this.homeOrAway = homeOrAway;
	}

	@Override
	public int compareTo(FixtureHighScorer other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		
		int thisToScoreRating=-1;
		int otherToScoreRating=-1;
		
		if (homeOrAway.isHome()) {
			thisToScoreRating = this.getHomeTeamToScoreRating();
			otherToScoreRating = other.getHomeTeamToScoreRating();
		} else {
			thisToScoreRating = this.getAwayTeamToScoreRating();
			otherToScoreRating = other.getAwayTeamToScoreRating();
		}
		
		return (otherToScoreRating - thisToScoreRating);
	}

	public int getHomeTeamToScoreRating() {
		return homeTeamToScoreRating;
	}

	public int getAwayTeamToScoreRating() {
		return awayTeamToScoreRating;
	}

}
