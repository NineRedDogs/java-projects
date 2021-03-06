package dogs.red.nine.oracle.forecast;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.FixtureData;
import dogs.red.nine.oracle.data.tables.TableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forecaster {

    private static final Logger logger = LogManager.getLogger("Forecaster");

    private final TableManager tableManager;
    private final List<FixtureData> forecastFixtures;

    public Forecaster(TableManager tabGen) {
        this.tableManager = tabGen;
        this.forecastFixtures = new ArrayList<FixtureData>();
    }

    public void forecast(List<FixtureData> fixtures) {

        Win w = new Win();
        generateForecastData(fixtures);
        //displayForecastData();

        BTTS btts = new BTTS();
        btts.process(forecastFixtures);
        btts.displayTips("BTTS tips");

        HomeWin hw = new HomeWin();
        hw.process(forecastFixtures);
        hw.displayTips("Home Win tips");

        // All wins (part 1) - add HOME win tips
        w.addHomeWinTips(hw);

        AwayWin aw = new AwayWin();
        aw.process(forecastFixtures);
        aw.displayTips("Away Win tips");

        // All wins (part 2) - add AWAY win tips
        w.addAwayWinTips(aw);

        // All wins (part 3) - DISPLAY collated tips
        w.displayTips("To Win tips");

        HomeHighScore hhi = new HomeHighScore();
        hhi.process(forecastFixtures);
        hhi.displayTips("Home HiScore tips");

        HomeLowScore hlo = new HomeLowScore();
        hlo.process(forecastFixtures);
        hlo.displayTips("Home LoScore tips");

        AwayHighScore ahi = new AwayHighScore();
        ahi.process(forecastFixtures);
        ahi.displayTips("Away HiScore tips");

        AwayLowScore alo = new AwayLowScore();
        alo.process(forecastFixtures);
        alo.displayTips("Away LoScore tips");

        MatchGoalsAbove2point5 above2p5 = new MatchGoalsAbove2point5();
        above2p5.process(forecastFixtures);
        above2p5.displayTips("Above 2.5 goals tips");

        MatchGoalsBelow2point5 below2p5 = new MatchGoalsBelow2point5();
        below2p5.process(forecastFixtures);
        below2p5.displayTips("Below 2.5 goals tips");

        /**String predictionsFor="All fixtures";
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
         logger.info("\nShowing predictions for " + predictionsFor + "\n\n");*/
    }

    private void displayForecastData() {
        logger.debug("----Forecast data-----------------------------------------");
        for (FixtureData fixture : forecastFixtures) {
            logger.debug("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
            logger.debug(fixture);
        }
    }


    private void generateForecastData(List<FixtureData> fixtures) {
        // get todays date
        final LocalDate todayDate = LocalDate.now();

        /** This section will iterate through all the fixtures retrieved and */
        for (FixtureData fixture : fixtures) {
            boolean shouldWeUseThisFixture = false;

            if (tableManager.getConfig().useJustTodaysGames()) {
                shouldWeUseThisFixture = fixture.getDate().equals(todayDate);
            } else {
                shouldWeUseThisFixture = !fixture.getDate().isBefore(todayDate);
            }

            // only check if fixture has not yet happened
            if (AppConstants.DEV_MODE || AppConstants.DEV_MODE_USE_THIS_WEEKS_PLAYED_FIXTURES || shouldWeUseThisFixture) {

                fixture.setForecastData(getFixtureData(fixture));
                forecastFixtures.add(fixture);
            }
        }
    }

    private FixtureForecastData getFixtureData(FixtureData fixture) {
        TeamForecastData htData = getTeamForecastData(fixture.getHomeTeam(), true, fixture.getDivision());
        TeamForecastData atData = getTeamForecastData(fixture.getAwayTeam(), false, fixture.getDivision());
        FixtureForecastData fData = new FixtureForecastData(htData, atData);

        return fData;
    }

    private TeamForecastData getTeamForecastData(String teamName, boolean isHomeTeam, Division division) {
        TeamForecastData teamForecastData = new TeamForecastData();
        if (isHomeTeam) {
            // 1a. add home form
            teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tableManager.getHomeFormData(teamName, division));
        } else {
            // 1b. add away form
            teamForecastData.addTeamForecastData(TeamForecastData.FORM_VENUE, tableManager.getAwayFormData(teamName, division));
        }

        // 2. add general current form
        teamForecastData.addTeamForecastData(TeamForecastData.FORM_GENERAL, tableManager.getFormData(teamName, division));

        if (isHomeTeam) {
            // 3a. add season home form
            teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tableManager.getHomeSeasonData(teamName, division));
        } else {
            // 3b. add season away form
            teamForecastData.addTeamForecastData(TeamForecastData.SEASON_VENUE, tableManager.getAwaySeasonData(teamName, division));
        }
        return teamForecastData;
    }


    private Date getTodaysDate() {
        Date todayDate = null;
        try {
            todayDate = AppConstants.dateFormatter.parse(AppConstants.dateFormatter.format(new Date()));
        } catch (ParseException e) {
            logger.debug("Error formatting date, e:" + e.getLocalizedMessage());
        }
        return todayDate;
    }


}
