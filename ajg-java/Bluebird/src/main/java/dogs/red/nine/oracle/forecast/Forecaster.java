package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forecaster {

    private static final Logger logger = LogManager.getLogger("Forecaster");

    private final TableGenerator tabGen;
    private final List<FixtureData> forecastFixtures;

    public Forecaster(TableGenerator tabGen) {
        this.tabGen = tabGen;
        this.forecastFixtures = new ArrayList<FixtureData>();
    }

    public void forecast(List<FixtureData> fixtures) {

        generateForecastData(fixtures);
        displayForecastData();

        BTTS btts = new BTTS();
        btts.process(forecastFixtures);

        String predictionsFor="All fixtures";
        if (AppConstants.DEV_MODE) {
            predictionsFor="sample fixtures (DEV MODE enabled)";
        } else if (AppConstants.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES) {
            predictionsFor="this weeks fixtures (DEV MODE)";
        } else if (AppConstants.ONLY_TODAYS_GAMES) {

            final Date todayDate = getTodaysDate();
            AppConstants.dateFormatter.applyPattern("EEEE d MMM yyyy");
            String myDate = AppConstants.dateFormatter.format(todayDate);

            predictionsFor="fixtures played on " + myDate;
        }
        logger.info("\nShowing predictions for " + predictionsFor + "\n\n");
    }

    private void displayForecastData() {
        logger.debug("----Forecast data-----------------------------------------");
        for (FixtureData fixture : forecastFixtures) {
            logger.debug("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
            logger.debug(fixture);
        }
    }


    private void generateForecastData(List<FixtureData> fixtures)  {
        // get todays date
        final Date todayDate = getTodaysDate();

        /** This section will iterate through all the fixtures retrieved and */
        for (FixtureData fixture : fixtures) {
            boolean shouldWeUseThisFixture = (!fixture.getDate().before(todayDate));

            // only check if fixture has not yet happened
            if (AppConstants.DEV_MODE || AppConstants.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES || shouldWeUseThisFixture) {

                fixture.setForecastData(getFixtureData(fixture));
                forecastFixtures.add(fixture);
            }
        }
    }

    private FixtureForecastData getFixtureData(FixtureData fixture) {
        TeamForecastData htData = getTeamForecastData(fixture.getHomeTeam(), true);
        TeamForecastData atData = getTeamForecastData(fixture.getAwayTeam(), false);
        FixtureForecastData fData = new FixtureForecastData(htData, atData);

        return fData;
    }

    private TeamForecastData getTeamForecastData(String teamName, boolean isHomeTeam) {
        TeamForecastData teamForecastData = new TeamForecastData();
        if (isHomeTeam) {
            // 1a. add home form
            teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tabGen.getHomeFormData(teamName));
        } else {
            // 1b. add away form
            teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tabGen.getAwayFormData(teamName));
        }

        // 2. add general current form
        teamForecastData.addTeamForecastData(TeamForecastData.FORM_GENERAL, tabGen.getFormData(teamName));

        if (isHomeTeam) {
            // 3a. add season home form
            teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tabGen.getHomeSeasonData(teamName));
        } else {
            // 3b. add season away form
            teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tabGen.getAwaySeasonData(teamName));
        }
        return teamForecastData;
    }


    private Date getTodaysDate() {
        Date todayDate = null;
        try {
            todayDate = AppConstants.dateFormatter.parse(AppConstants.dateFormatter.format(new Date() ));
        } catch (ParseException e) {
            logger.debug("Error formatting date, e:" + e.getLocalizedMessage());
        }
        return todayDate;
    }


}
