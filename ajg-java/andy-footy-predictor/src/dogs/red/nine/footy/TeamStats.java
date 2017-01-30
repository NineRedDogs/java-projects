package dogs.red.nine.footy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TeamStats {
	private static final Logger logger = LogManager.getLogger("TeamStats");
	
	private final int numGamesToUse;
	
	private int gamesPlayed=0;
	private int gamesPlayedHome=0;
	private int gamesPlayedAway=0;

	private int gamesScored=0;
	private int gamesScoredHome=0;
	private int gamesScoredAway=0;
	
	private int gamesConceded=0;
	private int gamesConcededHome=0;
	private int gamesConcededAway=0;

	private int goalsScored=0;
	private int goalsScoredHome=0;
	private int goalsScoredAway=0;
	private float goalsScoredX=0;
	private float goalsScoredHomeX=0;
	private float goalsScoredAwayX=0;
	
	private int goalsPerGameRating=0;
	private int goalsPerHomeGameRating=0;
	private int goalsPerAwayGameRating=0;

	private int pctgeGamesScoredIn=0;
	private int pctgeHomeGamesScoredIn=0;
	private int pctgeAwayGamesScoredIn=0;
	
	private StringBuilder allGamesGoalsHistory = new StringBuilder("");
	private StringBuilder homeGamesGoalsHistory = new StringBuilder("");
	private StringBuilder awayGamesGoalsHistory = new StringBuilder("");
	
	private int goalsConceded=0;
	private int goalsConcededHome=0;
	private int goalsConcededAway=0;
	private float goalsConcededX=0;
	private float goalsConcededHomeX=0;
	private float goalsConcededAwayX=0;
	
	private int goalsConcededPerGameRating=0;
	private int goalsConcededPerHomeGameRating=0;
	private int goalsConcededPerAwayGameRating=0;

	private int pctgeGamesConcededIn=0;
	private int pctgeHomeGamesConcededIn=0;
	private int pctgeAwayGamesConcededIn=0;
	
	private StringBuilder allGamesGoalsConcededHistory = new StringBuilder("");
	private StringBuilder homeGamesGoalsConcededHistory = new StringBuilder("");
	private StringBuilder awayGamesGoalsConcededHistory = new StringBuilder("");
	
	private int toScoreRating=0;
	private int toScoreHomeRating=0;
	private int toScoreAwayRating=0;
	private int toConcedeRating=0;
	private int toConcedeHomeRating=0;
	private int toConcedeAwayRating=0;
	
	private int toScoreHomeRatingX=0;
	private int toScoreAwayRatingX=0;
	private int toConcedeHomeRatingX=0;
	private int toConcedeAwayRatingX=0;


	private final String teamName;

	private Results results;
	private static final int ALL_SEASON_STATS = -1;

	public TeamStats(final String name, final int numGames) {
		this.teamName = name;
		this.numGamesToUse = numGames;
	}

	public TeamStats(final String name) {
		this(name, ALL_SEASON_STATS);
	}

	public void calcStats(final Results res) {
		this.results = res;
		updateStats();
	}

	private void updateStats() {
		
		if (this.numGamesToUse != ALL_SEASON_STATS) {
			//results.dumpList("Before trim");
			results.trim(this.numGamesToUse);
			//results.dumpList("After Trim");
		}

		this.gamesPlayed = results.getResults().size();
		int currGame = gamesPlayed;
		
		for (MatchData result : results.getResults()) {

			float gameMultiplier = ((float) currGame / (float) gamesPlayed);

			boolean homeTeam = (this.teamName.equalsIgnoreCase(result.getHomeTeam().getName()));
			
			if (homeTeam) {
				this.goalsScored += result.getHomeTeamScore();
				this.goalsScoredX += (result.getHomeTeamScore() * gameMultiplier);
				this.goalsScoredHome += result.getHomeTeamScore(); 
				this.goalsScoredHomeX += (result.getHomeTeamScore() * gameMultiplier);; 
				this.goalsConceded += result.getAwayTeamScore();
				this.goalsConcededX += (result.getAwayTeamScore() * gameMultiplier);
				this.goalsConcededHome += result.getAwayTeamScore();
				this.goalsConcededHomeX += (result.getAwayTeamScore() * gameMultiplier);
				this.gamesPlayedHome++;
				if (result.getHomeTeamScore() > 0) {
					this.gamesScored++;
					this.gamesScoredHome++;
				}
				if (result.getAwayTeamScore() > 0) {
					this.gamesConceded++;
					this.gamesConcededHome++;
				}
				this.allGamesGoalsHistory.append(result.getHomeTeamScore() + "  ");
				this.allGamesGoalsConcededHistory.append(result.getAwayTeamScore() + "  ");
				this.homeGamesGoalsHistory.append(result.getHomeTeamScore() + "  ");
				this.homeGamesGoalsConcededHistory.append(result.getAwayTeamScore() + "  ");
			} else {
				this.goalsScored += result.getAwayTeamScore(); 
				this.goalsScoredX += (result.getAwayTeamScore() * gameMultiplier); 
				this.goalsScoredAway += result.getAwayTeamScore(); 
				this.goalsScoredAwayX += (result.getAwayTeamScore() * gameMultiplier); 
				this.goalsConceded += result.getHomeTeamScore();
				this.goalsConcededX += (result.getHomeTeamScore() * gameMultiplier);
				this.goalsConcededAway += result.getHomeTeamScore();
				this.goalsConcededAwayX += (result.getHomeTeamScore() * gameMultiplier);
				this.gamesPlayedAway++;
				if (result.getAwayTeamScore() > 0) {
					this.gamesScored++;
					this.gamesScoredAway++;
				}
				if (result.getHomeTeamScore() > 0) {
					this.gamesConceded++;
					this.gamesConcededAway++;
				}
				this.allGamesGoalsHistory.append(result.getAwayTeamScore() + "  ");
				this.allGamesGoalsConcededHistory.append(result.getHomeTeamScore() + "  ");
				this.awayGamesGoalsHistory.append(result.getAwayTeamScore() + "  ");
				this.awayGamesGoalsConcededHistory.append(result.getHomeTeamScore() + "  ");
			}
			currGame--;
		}

		try {
			this.goalsPerGameRating=((this.goalsScored*100)/this.gamesPlayed);
			this.goalsPerHomeGameRating=((this.goalsScoredHome*100)/this.gamesPlayedHome);
			this.goalsPerAwayGameRating=((this.goalsScoredAway*100)/this.gamesPlayedAway);

			this.pctgeGamesScoredIn=((this.gamesScored*100)/this.gamesPlayed);
			this.pctgeHomeGamesScoredIn=((this.gamesScoredHome*100)/this.gamesPlayedHome);
			this.pctgeAwayGamesScoredIn=((this.gamesScoredAway*100)/this.gamesPlayedAway);

			this.goalsConcededPerGameRating=((this.goalsConceded*100)/this.gamesPlayed);
			this.goalsConcededPerHomeGameRating=((this.goalsConcededHome*100)/this.gamesPlayedHome);
			this.goalsConcededPerAwayGameRating=((this.goalsConcededAway*100)/this.gamesPlayedAway);

			this.pctgeGamesConcededIn=((this.gamesConceded*100)/this.gamesPlayed);
			this.pctgeHomeGamesConcededIn=((this.gamesConcededHome*100)/this.gamesPlayedHome);
			this.pctgeAwayGamesConcededIn=((this.gamesConcededAway*100)/this.gamesPlayedAway);
		} catch (Exception e) {
			System.out.println("Team : " + this.teamName + " --> Exception: " + e.getMessage());
		}

		// now the magic ......
		this.toScoreRating = ((this.goalsPerGameRating * this.pctgeGamesScoredIn) / 100 );
		this.toScoreHomeRating = ((this.goalsPerHomeGameRating * this.pctgeHomeGamesScoredIn) / 100 );
		this.toScoreAwayRating = ((this.goalsPerAwayGameRating * this.pctgeAwayGamesScoredIn) / 100 );

		this.toConcedeRating = ((this.goalsConcededPerGameRating * this.pctgeGamesConcededIn) / 100 );
		this.toConcedeHomeRating = ((this.goalsConcededPerHomeGameRating * this.pctgeHomeGamesConcededIn) / 100 );
		this.toConcedeAwayRating = ((this.goalsConcededPerAwayGameRating * this.pctgeAwayGamesConcededIn) / 100 );

		float shx = (((this.goalsScoredHomeX * ((float) this.pctgeHomeGamesScoredIn)) / 100 ) / this.gamesPlayedHome) * 100;
		float sax = (((this.goalsScoredAwayX * ((float) this.pctgeAwayGamesScoredIn)) / 100 ) / this.gamesPlayedAway) * 100;

		this.toScoreHomeRatingX = Math.round(shx);
		this.toScoreAwayRatingX = Math.round(sax);

		float chx = (((this.goalsConcededHomeX * ((float) this.pctgeHomeGamesConcededIn)) / 100 ) / this.gamesPlayedHome) * 100;
		float cax = (((this.goalsConcededAwayX * ((float) this.pctgeAwayGamesConcededIn)) / 100 ) / this.gamesPlayedAway) * 100;
	
		this.toConcedeHomeRatingX = Math.round(chx);
		this.toConcedeAwayRatingX = Math.round(cax);
	}

	


	public String getTotalStats() {
		StringBuilder sb = new StringBuilder(String.format("%-20s", this.teamName) + "All stats: ");
		sb.append("P: " + String.format("%3d", this.gamesPlayed));
		sb.append(" F: " + String.format("%3d", this.goalsScored));
		sb.append(" Fx: " + String.format("%4.2f", this.goalsScoredX));
		sb.append(" f/G: " + String.format("%3d", this.goalsPerGameRating));
		sb.append(" A: " + String.format("%3d", this.goalsConceded));
		sb.append(" Ax: " + String.format("%4.2f", this.goalsConcededX));
		sb.append(" a/G: " + String.format("%3d", this.goalsConcededPerGameRating));
		sb.append( "gS: " + String.format("%3d", this.gamesScored));
		sb.append(" %S: " + String.format("%3d", this.pctgeGamesScoredIn));
		sb.append(" gC: " + String.format("%3d", this.gamesConceded));
		sb.append(" %C: " + String.format("%3d", this.pctgeGamesConcededIn));
		sb.append(" F: " + this.allGamesGoalsHistory.toString());
		sb.append(" A: " + this.allGamesGoalsConcededHistory.toString());
		sb.append(" !S! " + String.format("%3d", this.toScoreRating));
		sb.append(" !C! " + String.format("%3d", this.toConcedeRating));
		return sb.toString();
	}

	public String getHomeStats() {
		StringBuilder sb = new StringBuilder(String.format("%-20s", this.teamName) + "Home stats: ");
		sb.append("P: " + String.format("%3d", this.gamesPlayedHome));
		sb.append(" F: " + String.format("%3d", this.goalsScoredHome));
		sb.append(" Fx: " + String.format("%4.2f", this.goalsScoredHomeX));
		sb.append(" f/G: " + String.format("%3d", this.goalsPerHomeGameRating));
		sb.append(" A: " + String.format("%3d", this.goalsConcededHome));
		sb.append(" Ax: " + String.format("%4.2f", this.goalsConcededHomeX));
		sb.append(" a/G: " + String.format("%3d", this.goalsConcededPerHomeGameRating));
		sb.append(" gS: " + String.format("%3d", this.gamesScoredHome));
		sb.append(" %S: " + String.format("%3d", this.pctgeHomeGamesScoredIn));
		sb.append(" gC: " + String.format("%3d", this.gamesConcededHome));
		sb.append(" %C: " + String.format("%3d", this.pctgeHomeGamesConcededIn));
		sb.append(" Fh: " + this.homeGamesGoalsHistory.toString());
		sb.append(" Ah: " + this.homeGamesGoalsConcededHistory.toString());
		sb.append(" !S! " + String.format("%3d", this.toScoreHomeRating));
		sb.append(" !C! " + String.format("%3d", this.toConcedeHomeRating));
		return sb.toString();
	}

	public String getAwayStats() {
		StringBuilder sb = new StringBuilder(String.format("%-20s", this.teamName) + "Away stats: ");
		sb.append("P: " + String.format("%3d", this.gamesPlayedAway));
		sb.append(" F: " + String.format("%3d", this.goalsScoredAway));
		sb.append(" Fx: " + String.format("%4.2f", this.goalsScoredAwayX));
		sb.append(" f/G: " + String.format("%3d", this.goalsPerAwayGameRating));
		sb.append(" A: " + String.format("%3d", this.goalsConcededAway));
		sb.append(" Ax: " + String.format("%4.2f", this.goalsConcededAwayX));
		sb.append(" a/G: " + String.format("%3d", this.goalsConcededPerAwayGameRating));
		sb.append(" gS: " + String.format("%3d", this.gamesScoredAway));
		sb.append(" %S: " + String.format("%3d", this.pctgeAwayGamesScoredIn));
		sb.append(" gC: " + String.format("%3d", this.gamesConcededAway));
		sb.append(" %C: " + String.format("%3d", this.pctgeAwayGamesConcededIn));
		sb.append(" Aa: " + this.awayGamesGoalsConcededHistory.toString());
		sb.append(" Fa: " + this.awayGamesGoalsHistory.toString());
		sb.append(" !C! " + String.format("%3d", this.toConcedeAwayRating));
		sb.append(" !S! " + String.format("%3d", this.toScoreAwayRating));
		return sb.toString();
	}
	
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}

	public int getGamesPlayedHome() {
		return this.gamesPlayedHome;
	}

	public int getGamesPlayedAway() {
		return this.gamesPlayedAway;
	}

	public int getGamesScored() {
		return this.gamesScored;
	}

	public int getGamesScoredHome() {
		return this.gamesScoredHome;
	}

	public int getGamesScoredAway() {
		return this.gamesScoredAway;
	}

	public int getGamesConceded() {
		return this.gamesConceded;
	}

	public int getGamesConcededHome() {
		return this.gamesConcededHome;
	}

	public int getGamesConcededAway() {
		return this.gamesConcededAway;
	}

	public int getGoalsScored() {
		return this.goalsScored;
	}

	public int getGoalsScoredHome() {
		return this.goalsScoredHome;
	}

	public int getGoalsScoredAway() {
		return this.goalsScoredAway;
	}

	public float getGoalsScoredX() {
		return this.goalsScoredX;
	}

	public float getGoalsScoredHomeX() {
		return this.goalsScoredHomeX;
	}

	public float getGoalsScoredAwayX() {
		return this.goalsScoredAwayX;
	}

	public int getGoalsPerGameRating() {
		return this.goalsPerGameRating;
	}

	public int getGoalsPerHomeGameRating() {
		return this.goalsPerHomeGameRating;
	}

	public int getGoalsPerAwayGameRating() {
		return this.goalsPerAwayGameRating;
	}

	public int getPctgeGamesScoredIn() {
		return this.pctgeGamesScoredIn;
	}

	public int getPctgeHomeGamesScoredIn() {
		return this.pctgeHomeGamesScoredIn;
	}

	public int getPctgeAwayGamesScoredIn() {
		return this.pctgeAwayGamesScoredIn;
	}

	public StringBuilder getAllGamesGoalsHistory() {
		return this.allGamesGoalsHistory;
	}

	public StringBuilder getHomeGamesGoalsHistory() {
		return this.homeGamesGoalsHistory;
	}

	public StringBuilder getAwayGamesGoalsHistory() {
		return this.awayGamesGoalsHistory;
	}

	public int getGoalsConceded() {
		return this.goalsConceded;
	}

	public int getGoalsConcededHome() {
		return this.goalsConcededHome;
	}

	public int getGoalsConcededAway() {
		return this.goalsConcededAway;
	}

	public float getGoalsConcededX() {
		return this.goalsConcededX;
	}

	public float getGoalsConcededHomeX() {
		return this.goalsConcededHomeX;
	}

	public float getGoalsConcededAwayX() {
		return this.goalsConcededAwayX;
	}


	public int getGoalsConcededPerGameRating() {
		return this.goalsConcededPerGameRating;
	}

	public int getGoalsConcededPerHomeGameRating() {
		return this.goalsConcededPerHomeGameRating;
	}

	public int getGoalsConcededPerAwayGameRating() {
		return this.goalsConcededPerAwayGameRating;
	}

	public int getPctgeGamesConcededIn() {
		return this.pctgeGamesConcededIn;
	}

	public int getPctgeHomeGamesConcededIn() {
		return this.pctgeHomeGamesConcededIn;
	}

	public int getPctgeAwayGamesConcededIn() {
		return this.pctgeAwayGamesConcededIn;
	}

	public StringBuilder getAllGamesGoalsConcededHistory() {
		return this.allGamesGoalsConcededHistory;
	}

	public StringBuilder getHomeGamesGoalsConcededHistory() {
		return this.homeGamesGoalsConcededHistory;
	}

	public StringBuilder getAwayGamesGoalsConcededHistory() {
		return this.awayGamesGoalsConcededHistory;
	}

	public int getToScoreRating() {
		return this.toScoreRating;
	}

	public int getToScoreHomeRating() {
		return this.toScoreHomeRating;
	}

	public int getToScoreAwayRating() {
		return this.toScoreAwayRating;
	}

	public int getToScoreHomeRatingX() {
		return this.toScoreHomeRatingX;
	}

	public int getToScoreAwayRatingX() {
		return this.toScoreAwayRatingX;
	}

	public int getToConcedeHomeRatingX() {
		return this.toConcedeHomeRatingX;
	}

	public int getToConcedeAwayRatingX() {
		return this.toConcedeAwayRatingX;
	}

	public int getToConcedeRating() {
		return this.toConcedeRating;
	}

	public int getToConcedeHomeRating() {
		return this.toConcedeHomeRating;
	}

	public int getToConcedeAwayRating() {
		return this.toConcedeAwayRating;
	}

	@Override
	public String toString() {
		return this.teamName;
		//return teamName + ", results=" + getResults() + "]";
	}

	public String showStats() {
		StringBuilder sb = new StringBuilder(this.teamName + ":");
		sb.append(getTotalStats() + "\n");
		sb.append(getHomeStats() + "\n");
		sb.append(getAwayStats() + "\n");
		
		return sb.toString();
	}



}
