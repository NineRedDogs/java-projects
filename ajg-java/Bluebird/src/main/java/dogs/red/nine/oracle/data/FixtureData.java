package dogs.red.nine.oracle.data;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.forecast.FixtureForecastData;
import dogs.red.nine.oracle.general.DisplayExtras;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FixtureData implements Cloneable {

	private static final Logger logger = LogManager.getLogger("FixtureData");

	private static final String DATA_SEPARATOR_CHAR = ",";
	protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
	protected static final DateTimeFormatter prettyDateFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_PRETTY);


	private Division division;
	private LocalDate date;
	private String homeTeam;
	private String awayTeam;
	private FixtureForecastData forecastData;


	/**
	 * @param division
	 * @param date
	 * @param homeTeam
	 * @param awayTeam
	 */
	public FixtureData(Division division, LocalDate date, String homeTeam, String awayTeam) {
		this.division = division;
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	public FixtureData() {	}

	public FixtureData(FixtureData hwTip) {
		this.division = hwTip.division;
		this.date = hwTip.date;
		this.homeTeam = hwTip.homeTeam;
		this.awayTeam = hwTip.awayTeam;
		this.forecastData = new FixtureForecastData(hwTip.forecastData);
	}

	public void highlightHomeTeam() {
		this.homeTeam = highlight(this.homeTeam);
	}

	public void highlightAwayTeam() {
		this.awayTeam = highlight(this.awayTeam);
	}

	private String highlight(String textToHighlight) {
		return textToHighlight.toUpperCase();
	}

	public FixtureData(String lineReadFromDataFile, String[] keyData) throws ParseException, NumberFormatException {
		String[] matchFixtureElems = lineReadFromDataFile.split(DATA_SEPARATOR_CHAR,-1);

		if (keyData == null) {
			throw new ParseException("match fixture parse problem : key data is null !!", 0);
		}

		if (matchFixtureElems.length != keyData.length) {
			throw new ParseException("match fixture mismatch : num key elements : " + keyData.length + " match fixture elems : " + matchFixtureElems.length, 0);
		}

		int colNum=0;
		for (String key : keyData) {

			switch (key) {

				case "Div" :
					division = Division.fromString(matchFixtureElems[colNum]);
					break;

				case "Date" :
					date = LocalDate.parse(matchFixtureElems[colNum], dateFormatter);
					break;

				case "HomeTeam" :
					homeTeam = matchFixtureElems[colNum];
					break;

				case "AwayTeam" :
					awayTeam = matchFixtureElems[colNum];
					break;

				default:
					//System.out.println("Unsupported column : " + matchResultElems[colNum]);
					break;
			}
			// move on to next column
			colNum++;
		}
	}

	public Object clone()throws CloneNotSupportedException{
		return (FixtureData)super.clone();
	}


	/**
	 * Getters/Setters ------------------------------------------
	 * */

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public FixtureForecastData getForecastData() {
		return forecastData;
	}

	public void setForecastData(FixtureForecastData fixtureForecastData) {
		this.forecastData = fixtureForecastData;
	}

	/**
	 * Local methods  ----------------------------
	 */


	private boolean hasTeam(String team) {
		return (team.equals(homeTeam) || team.equals(awayTeam));
	}


	/**
	 * Formatting/Output methods --------------------------------
	 */

	@Override
	public String toString() {
		return asStringBasic() + "\n" + forecastData;
	}

	public String asStringBasic() {
		return division + " (" + date.format(prettyDateFormatter) + ") " + homeTeam + " v "	+ awayTeam;
	}

	public String asString(boolean justForecastScore) {
		final String forecastString = (justForecastScore) ? forecastData.getForecastScoreAsString() : "\n" + forecastData.toString();
		return asStringBasic() + " : " + forecastString;
	}

	public String fixturePrint() {
		return fixturePrint(DisplayExtras.PlainText);
	}

	private String getDateAndDivFixed() {
		StringBuilder sb = new StringBuilder();
		sb.append(date.format(prettyDateFormatter) + " ");
		sb.append(division + ": ");
		return String.format("%-42s", sb.toString());
	}

	public String fixturePrint(DisplayExtras displayExtras) {
		StringBuilder sb = new StringBuilder(getDateAndDivFixed());
		sb.append(homeTeam + " v " + awayTeam);
		return sb.toString();
	}

}
