package dogs.red.nine.oracle.data;

import dogs.red.nine.oracle.general.DisplayExtras;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FixtureData {
	
	private static final String DATA_SEPARATOR_CHAR = ",";
	
	private Division division;
	private Date date;
	private Team homeTeam;
	private Team awayTeam;
	

	/**
	 * @param division
	 * @param date
	 * @param homeTeam
	 * @param awayTeam
	 */
	public FixtureData(Division division, Date date, Team homeTeam,
			Team awayTeam) {
		this.division = division;
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	public FixtureData() {
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
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    			date = dateFormat.parse(matchFixtureElems[colNum]);
				break;

			case "HomeTeam" : 
				homeTeam = new Team(matchFixtureElems[colNum]);
				break;

			case "AwayTeam" : 
				awayTeam = new Team(matchFixtureElems[colNum]);
				break;
				
				default:
					//System.out.println("Unsupported column : " + matchResultElems[colNum]);
					break;
			}
			// move on to next column
			colNum++;
		}
	}


	public Division getDivision() {
		return division;
	}



	public Date getDate() {
		return date;
	}



	public Team getHomeTeam() {
		return homeTeam;
	}



	public void setDivision(Division division) {
		this.division = division;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}



	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}



	public Team getAwayTeam() {
		return awayTeam;
	}


	public boolean hasTeam(final String teamName) {
		return hasTeam(new Team(teamName));
	}


	private boolean hasTeam(Team team) {
		return (team.equals(homeTeam) || team.equals(awayTeam));
	}


	@Override
	public String toString() {
		return division + " (" + new SimpleDateFormat("EEE d MMM").format(date) + ") " + homeTeam + " v " + awayTeam;
	}

	public String fixturePrint() {
		return fixturePrint(DisplayExtras.PlainText);
	}

	private String getDateAndDivFixed() {
		StringBuilder sb = new StringBuilder();
		sb.append(new SimpleDateFormat("EEE MMM d yyyy").format(date) + " ");
		sb.append(division + ": ");
		return String.format("%-42s", sb.toString());
	}
	
	public String fixturePrint(DisplayExtras displayExtras) {
		StringBuilder sb = new StringBuilder(getDateAndDivFixed());
		sb.append(homeTeam.getName(displayExtras.isHlHome()) + " v " + awayTeam.getName(displayExtras.isHlAway()));
		return sb.toString();
	}
	
}
