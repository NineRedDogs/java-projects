package dogs.red.nine.oracle.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TableEntry implements Comparable<TableEntry> {

    private final String teamName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    private static final Logger logger = LogManager.getLogger("TableEntry");


    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setGamesDrawn(int gamesDrawn) {
        this.gamesDrawn = gamesDrawn;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesDrawn() {
        return gamesDrawn;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getGoalDifference() {
        return (goalsFor - goalsAgainst);
    }

    public int getPoints() {
        return points;
    }

    public TableEntry(String teamName) {
        this.teamName = teamName;
    }

    public void addResult(final MatchData result) {
        if (teamName.equalsIgnoreCase(result.getHomeTeam())) {
            // team is home team
            add(result.getHomeTeamScore(), result.getAwayTeamScore());

        } else if (teamName.equalsIgnoreCase(result.getAwayTeam())) {
            // team is away team
            add(result.getAwayTeamScore(), result.getHomeTeamScore());
        } else {
            logger.debug("current team not home OR away in given match !!!");
        }
    }

    private void add(int ourScore, int otherTeamScore) {
        gamesPlayed++;
        if (ourScore > otherTeamScore) {
            gamesWon++;
            points += 3;
        } else if (ourScore == otherTeamScore) {
            gamesDrawn++;
            points += 1;
        } else {
            gamesLost++;
        }
        goalsFor += ourScore;
        goalsAgainst += otherTeamScore;

    }


    @Override
    public int compareTo(TableEntry otherEntry) {
	// compareTo should return < 0 if this is supposed to be
		// less than other, > 0 if this is supposed to be greater than 
		// other and 0 if they are supposed to be equal
		
		if (getPoints() < otherEntry.getPoints()) {
			return -1;
		} else if (getPoints() > otherEntry.getPoints()) {
			return 1;
		} else {
            if (getGoalDifference() < otherEntry.getGoalDifference()) {
                return -1;
            } else if (getGoalDifference() > otherEntry.getGoalDifference()) {
                return 1;
            } else {
                if (getGoalsFor() < otherEntry.getGoalsFor()) {
                    return -1;
                } else if (getGoalDifference() > otherEntry.getGoalDifference()) {
                    return 1;
                }
            }
			// must be the same 
			return 0;
		}
    }




    
}
