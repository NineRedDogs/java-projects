package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.tables.TableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixtureForecastData {

    private static final Logger logger = LogManager.getLogger("FixtureForecastData");

    private final TeamForecastData htData;
    private final TeamForecastData atData;
    private float forecastScore;

    public FixtureForecastData(TeamForecastData htData, TeamForecastData atData) {
        this.htData = htData;
        this.atData = atData;
        this.forecastScore = 0.0f;
    }

    public FixtureForecastData(FixtureForecastData forecastData) {
        this.htData = new TeamForecastData(forecastData.htData);
        this.atData = new TeamForecastData(forecastData.atData);
        this.forecastScore = forecastData.forecastScore;
    }

    public TeamForecastData getHtData() {
        return htData;
    }

    public TeamForecastData getAtData() {
        return atData;
    }

    public float getForecastScore() {
        return forecastScore;
    }

    public void setForecastScore(float forecastScore) {
        this.forecastScore = forecastScore;
    }

    @Override
    public String toString() {
        return "----------------------------------------------------------------------------" +
                "\nHome Team " + TableEntry.formattedHeadersNoNameSpacers + "\n" + htData +
                "\nAway Team " + TableEntry.formattedHeadersNoNameSpacers + "\n" + atData +
               "\n forecastScore: [" + forecastScore + "]\n" +
                "----------------------------------------------------------------------------";
    }

    public String getForecastScoreAsString() {
        return " [" + forecastScore + "]";
    }
}
