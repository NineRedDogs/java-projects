package dogs.red.nine.oracle.data;

import dogs.red.nine.oracle.data.tables.TableEntry;

public class TeamForecastData {
    private final TableEntry formData;

    public TeamForecastData(TableEntry formData) {
        this.formData = formData;
    }

    @Override
    public String toString() {
        return "TeamForecastData{" +
                "formData=" + formData +
                '}';
    }
}
