package dogs.red.nine.footy;

import java.util.Date;

public class FixtureHighScorerX extends FixtureData implements Comparable<FixtureHighScorerX> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;
	private final HomeOrAway homeOrAway;

	public FixtureHighScorerX(Division division, Date date, Team homeTeam,
			Team awayTeam, HomeOrAway homeOrAway) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		this.homeOrAway = homeOrAway;
	}

	@Override
	public int compareTo(FixtureHighScorerX other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		
		int thisToScoreRating=-1;
		int otherToScoreRating=-1;
		
		if (homeOrAway.isHome()) {
			thisToScoreRating = this.getHomeTeamToScoreRating();
		} else {
			thisToScoreRating = this.getAwayTeamToScoreRating();
		}
		
		if (other.homeOrAway.isHome()) {
			otherToScoreRating = other.getHomeTeamToScoreRating();
		} else {
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
	
	public boolean isHomeHS() {
		return homeOrAway == HomeOrAway.Home;
	}

	public boolean isAwayHS() {
		return homeOrAway == HomeOrAway.Away;
	}

}
