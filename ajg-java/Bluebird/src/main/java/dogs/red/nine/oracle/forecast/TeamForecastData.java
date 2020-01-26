package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.data.tables.TableEntry;

import java.util.HashMap;
import java.util.Map;

public class TeamForecastData {

    public static final int FORM_VENUE = 0;
    public static final int FORM_GENERAL = 1;
    public static final int SEASON_VENUE = 2;

    private final Map<Integer, TableEntry> teamData = new HashMap<Integer, TableEntry>();

    public void addTeamForecastData(int slot, TableEntry teamForecastData) {
        this.teamData.put(slot, teamForecastData);
    }

    public TableEntry getTeamForecastData(int slot) {
        return teamData.get(slot);
    }

    public String getFormDataAsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, TableEntry> entry : teamData.entrySet()) {
            sb.append("slot [" + entry.getKey() + "] " + entry.getValue() + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormDataAsString();
    }
}
