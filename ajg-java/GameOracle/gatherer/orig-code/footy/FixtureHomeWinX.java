package dogs.red.nine.footy;

import java.util.Date;

public class FixtureHomeWinX extends FixtureData implements Comparable<FixtureHomeWinX> {

	private final int homeTeamToScoreRating;
	private final int awayTeamToScoreRating;
	private final int homeTeamWinRating;

	public FixtureHomeWinX(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		super(division, date, homeTeam, awayTeam);
		homeTeamToScoreRating = homeTeam.getToScoreHomeRatingX() + awayTeam.getToConcedeAwayRatingX();
		awayTeamToScoreRating = awayTeam.getToScoreAwayRatingX() + homeTeam.getToConcedeHomeRatingX();
		
		homeTeamWinRating = (homeTeamToScoreRating - awayTeamToScoreRating);
	}

	@Override
	public int compareTo(FixtureHomeWinX other) {
		 // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		return (other.homeTeamWinRating - homeTeamWinRating);
	}

	public int getHomeTeamWinRating() {
		return homeTeamWinRating;
	}
}
