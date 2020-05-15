package dogs.red.nine.oracle;

public class Config {

    private final String leaguesToUse;

    private final int numCurrentFormGames;

    private final String season;
    private final boolean justTodaysGames;

    public Config(String leaguesToUse, int numCurrentFormGames, String season, boolean justTodaysGames) {
        this.leaguesToUse = leaguesToUse;
        this.numCurrentFormGames = numCurrentFormGames;
        this.season = season;
        this.justTodaysGames = justTodaysGames;
    }

    public String getLeaguesToUse() {
        return leaguesToUse;
    }

    public int getNumCurrentFormGames() {
        return numCurrentFormGames;
    }

    public String getSeason() {
        return season;
    }

    public boolean useJustTodaysGames() { return justTodaysGames; }

    @Override
    public String toString() {
        return "Config{" +
                "leaguesToUse='" + leaguesToUse + '\'' +
                ", numCurrentFormGames=" + numCurrentFormGames +
                ", season='" + season + '\'' +
                ", justTodaysGames=" + justTodaysGames +
                '}';
    }
}
