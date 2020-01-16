package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TableEntry implements Comparable<TableEntry> {

    public static String formattedHeaders = "                           P    W    D    L    F    A    GD   Pts";
    private final String teamName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    private static final Logger logger = LogManager.getLogger("TableEntry");


    protected void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    protected void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    protected void setGamesDrawn(int gamesDrawn) {
        this.gamesDrawn = gamesDrawn;
    }

    protected void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    protected void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    protected void incrementGamesWon() {
        this.gamesWon++;
    }

    protected void incrementGamesDrawn() {
        this.gamesDrawn++;
    }

    protected void incrementGamesLost() {
        this.gamesLost++;
    }

    protected void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    protected void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    protected void setPoints(int points) {
        this.points = points;
    }

    protected void incrementGoalsFor(int goalsFor) {
        this.goalsFor += goalsFor;
    }

    protected void incrementGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst += goalsAgainst;
    }

    protected void incrementPoints(int points) {
        this.points += points;
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


    public abstract void addResult(final MatchData result);


    @Override
    public int compareTo(TableEntry otherEntry) {
	// compareTo should return < 0 if this is supposed to be
		// less than other, > 0 if this is supposed to be greater than 
		// other and 0 if they are supposed to be equal
//        logger.debug("comparing " + this + " with " + otherEntry +  " ...");

		if (getPoints() < otherEntry.getPoints()) {
			return 1;
		} else if (getPoints() > otherEntry.getPoints()) {
			return -1;
		} else {
            if (getGoalDifference() < otherEntry.getGoalDifference()) {
                return 1;
            } else if (getGoalDifference() > otherEntry.getGoalDifference()) {
                return -1;
            } else {
                if (getGoalsFor() < otherEntry.getGoalsFor()) {
                    return 1;
                } else if (getGoalDifference() > otherEntry.getGoalDifference()) {
                    return -1;
                }
            }
			// must be the same 
			return 0;
		}
    }


    @Override
    public String toString() {
        return "     " + gamesPlayed +
                "  " + gamesWon +
                "  " + gamesDrawn +
                "  " + gamesLost +
                "  " + goalsFor +
                "  " + goalsAgainst +
                "  " + getGoalDifference() +
                "  " + points;
    }
}
