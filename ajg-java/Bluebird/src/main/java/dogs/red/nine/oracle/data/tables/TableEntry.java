package dogs.red.nine.oracle.data.tables;

import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.general.DisplayExtras;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public abstract class TableEntry implements Comparable<TableEntry> {

    private static String spacersForTeamName = "                        ";
    public static String formattedHeadersNoNameSpacers = "   P  Pts    QLTY    W    D    L    F    A   GD  Mag Btts   HS  HSo   GS  GSo   CS  CSo    MerS    MerR     PtR     MgR  AvSc";
    public static String formattedHeaders = spacersForTeamName + formattedHeadersNoNameSpacers;
    private final String teamName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
    private int highScoreOppoGames;
    private int gamesScoredOppo;
    private int cleanSheetsOppo;
    private int highScoreUsGames;
    private int gamesScoredUs;
    private int cleanSheetsUs;
    private int bttsGames;
    private float meritScore;

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

    protected void setMeritScore(float meritScore) {
        this.meritScore = meritScore;
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

    public static String getFormattedHeaders() {
        return formattedHeaders;
    }

    public int getHighScoreOppoGames() {
        return highScoreOppoGames;
    }

    public int getGamesScoredOppo() {
        return gamesScoredOppo;
    }

    public int getCleanSheetsOppo() {
        return cleanSheetsOppo;
    }

    public int getHighScoreUsGames() {
        return highScoreUsGames;
    }

    public int getGamesScoredUs() {
        return gamesScoredUs;
    }

    public int getCleanSheetsUs() {
        return cleanSheetsUs;
    }

    public int getBttsGames() {
        return bttsGames;
    }

    public float getMeritScore() {
        return meritScore;
    }

    public int getMagicNumber() {
        int magicNumber = getGamesWon() + getGoalsFor() + getPoints() - getGamesLost() - getGoalsAgainst();
        return magicNumber;
    }

    public float getQualityRating() {
        return ((getMagicRate() + getMeritRate()) / 2);
    }

    public float getMagicRate() {
        if (gamesPlayed == 0) {
            return 0.0f;
        } else {
            return ((float)getMagicNumber() / gamesPlayed);
        }
    }

    public float getPointsRate() {
        if (gamesPlayed == 0) {
            return 0.0f;
        } else {
            return ((float)points / (gamesPlayed * 3));
        }
    }

    public float getMeritRate() {
        // if no games played, set a start merit of 1.0
        return (getGamesPlayed() > 0 ? (getMeritScore() / getGamesPlayed()) : 0.0f);
    }

    public int getAvgeScoreFor() { return (doDiv(goalsFor, gamesPlayed)); }
    public int getAvgeScoreAgainst() { return (doDiv(goalsAgainst, gamesPlayed)); }

    public TableEntry(String teamName) {
        this.teamName = teamName;
    }

    private int doDiv(int a, int b) {
        float x = (float) ((double)a / b);
        int r = Math.round(x);
        return r;
    }

    public abstract void addResult(final MatchData result, final TableEntry otherTeam);

    protected void add(int ourScore, int otherTeamScore, final TableEntry otherTeam) {
        incrementGamesPlayed();
        if (ourScore > otherTeamScore) {
            incrementGamesWon();
            incrementPoints(3);
        } else if (ourScore == otherTeamScore) {
            incrementGamesDrawn();
            incrementPoints(1);
        } else {
            incrementGamesLost();
        }
        if ((ourScore > 0) && (otherTeamScore > 0)) {
            incrementBtts();
        }
        if (ourScore == 0) {
            incrementCleanSheetUs();
        } else {
            incrementGamesScoredUs();
            if (ourScore > 1) {
                incrementHighScoreUs();
            }
        }
        if (otherTeamScore == 0) {
            incrementCleanSheetOppo();
        } else {
            incrementGamesScoredOppo();
            if (otherTeamScore > 1) {
                incrementHighScoreOppo();
            }
        }

        incrementGoalsFor(ourScore);
        incrementGoalsAgainst(otherTeamScore);

        updateMeritScore(ourScore, otherTeamScore, otherTeam);
    }

    private void updateMeritScore(int ourScore, int otherTeamScore, final TableEntry otherTeam) {
        float meritDiff = meritScore - otherTeam.getMeritScore();
        float meritRateDiff = getMeritRate() - otherTeam.getMeritRate();
        boolean playedBetterTeam = (getMeritRate()  < otherTeam.getMeritRate());

        float addMeritScore = 0.0f;
        if (ourScore > otherTeamScore) {
            // win
            addMeritScore = (3 + otherTeam.getMeritRate());
        } else if (ourScore == otherTeamScore) {
            // draw
            addMeritScore = (1 + (otherTeam.getMeritRate()/2));
        } else {
            // loss
            // no change
        }

        meritScore += addMeritScore;
    }

    protected void incrementHighScoreOppo() {
        highScoreOppoGames++;
    }

    protected void incrementGamesScoredOppo() {
        gamesScoredOppo++;
    }

    protected void incrementCleanSheetOppo() {
        cleanSheetsOppo++;
    }

    protected void incrementHighScoreUs() {
        highScoreUsGames++;
    }

    protected void incrementGamesScoredUs() {
        gamesScoredUs++;
    }

    protected void incrementCleanSheetUs() {
        cleanSheetsUs++;
    }

    protected void incrementBtts() {
        bttsGames++;
    }


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
        return " " + formatInt(getGamesPlayed()) +
                " " + formatInt(getPoints()) +
                " " + formatFloat(getQualityRating()) +
                " " + formatInt(getGamesWon()) +
                " " + formatInt(getGamesDrawn()) +
                " " + formatInt(getGamesLost()) +
                " " + formatInt(getGoalsFor()) +
                " " + formatInt(getGoalsAgainst()) +
                " " + formatInt(getGoalDifference()) +
                " " + formatInt(getMagicNumber()) +
                " " + formatInt(getBttsGames()) +
                " " + formatInt(getHighScoreUsGames()) +
                " " + formatInt(getHighScoreOppoGames()) +
                " " + formatInt(getGamesScoredUs()) +
                " " + formatInt(getGamesScoredOppo()) +
                " " + formatInt(getCleanSheetsUs()) +
                " " + formatInt(getCleanSheetsOppo()) +
                " " + formatFloat(getMeritScore()) +
                " " + formatFloat(getMeritRate()) +
                " " + formatFloat(getPointsRate()) +
                " " + formatFloat(getMagicRate()) +
                " (" + getAvgeScoreFor() + ":" + getAvgeScoreAgainst() + ")";
    }

    protected String formatInt(int num) {
        return String.format("%4s", num);
    }
    private String formatFloat(float num) {
        return String.format(" %6.2f", num);
    }
    private String formatTeamName(String name) {
        return String.format("  %16s", name);
    }

    public String fullString() {
        return formatTeamName(teamName) + ":" + toString();
    }

}
