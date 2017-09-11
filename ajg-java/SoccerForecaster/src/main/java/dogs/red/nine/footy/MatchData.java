package dogs.red.nine.footy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MatchData extends FixtureData implements Comparable<MatchData> {
	
	private static final String DATA_SEPARATOR_CHAR = ",";
	/**
	 * Possible data entries are as follows : 
	 * 
	 * Key to results data:
	 * 
     *     Div = League Division
     *     Date = Match Date (dd/mm/yy)
     *     HomeTeam = Home Team
     *     AwayTeam = Away Team
     *     FTHG = Full Time Home Team Goals
     *     FTAG = Full Time Away Team Goals
     *     FTR = Full Time Result (H=Home Win, D=Draw, A=Away Win)
     *     HTHG = Half Time Home Team Goals
     *     HTAG = Half Time Away Team Goals
     *     HTR = Half Time Result (H=Home Win, D=Draw, A=Away Win)
     *     
     * Match Statistics (where available)
     *     Attendance = Crowd Attendance
     *     Referee = Match Referee
     *     HS = Home Team Shots
     *     AS = Away Team Shots
     *     HST = Home Team Shots on Target
     *     AST = Away Team Shots on Target
     *     HHW = Home Team Hit Woodwork
     *     AHW = Away Team Hit Woodwork
     *     HC = Home Team Corners
     *     AC = Away Team Corners
     *     HF = Home Team Fouls Committed
     *     AF = Away Team Fouls Committed
     *     HO = Home Team Offsides
     *     AO = Away Team Offsides
     *     HY = Home Team Yellow Cards
     *     AY = Away Team Yellow Cards
     *     HR = Home Team Red Cards
     *     AR = Away Team Red Cards
     *     HBP = Home Team Bookings Points (10 = yellow, 25 = red)
     *     ABP = Away Team Bookings Points (10 = yellow, 25 = red)
     *
	 */
	
	private MatchResult htResult;
	private MatchResult ftResult;
	
	private int homeTeamScore;
	private int homeTeamHTScore;
	private int awayTeamScore;
	private int awayTeamHTScore;
	
	
	public MatchData(String lineReadFromDataFile, String[] keyData) throws ParseException, NumberFormatException {
		super();
		
		String[] matchResultElems = lineReadFromDataFile.split(DATA_SEPARATOR_CHAR);
		
		if (keyData == null) {
			throw new ParseException("match result parse problem : key data is null !!", 0);
		}

		if (matchResultElems.length != keyData.length) {
			throw new ParseException("match result mismatch : num key elements : " + keyData.length + " match result elems : " + matchResultElems.length, 0);
		}
		
		int colNum=0;
		for (String key : keyData) {
			
			switch (key) {

			case "Div" : 
				setDivision(Division.fromString(matchResultElems[colNum]));
				break;
				
			case "Date" : 
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    			setDate(dateFormat.parse(matchResultElems[colNum]));
				break;

			case "FTR" : 
				ftResult = MatchResult.fromString(matchResultElems[colNum]);
				break;

			case "HTR" : 
				htResult = MatchResult.fromString(matchResultElems[colNum]);
				break;
			
			case "HomeTeam" : 
				setHomeTeam(new Team(matchResultElems[colNum]));
				break;

			case "FTHG" : 
				homeTeamScore = Integer.parseInt(matchResultElems[colNum]);
			    break;
			    
			case "HTHG" : 
				homeTeamHTScore = Integer.parseInt(matchResultElems[colNum]);
				break;
			
			case "AwayTeam" : 
				setAwayTeam(new Team(matchResultElems[colNum]));
				break;
				
			case "FTAG": 
				awayTeamScore = Integer.parseInt(matchResultElems[colNum]);
				break;

			case "HTAG": 
				awayTeamHTScore = Integer.parseInt(matchResultElems[colNum]);
				break;

				default:
					//System.out.println("Unsupported column : " + matchResultElems[colNum]);
					break;
			}
			// move on to next column
			colNum++;
		}
	}

	public MatchResult getHtResult() {
		return htResult;
	}



	public MatchResult getFtResult() {
		return ftResult;
	}



	public int getHomeTeamScore() {
		return homeTeamScore;
	}



	public int getHomeTeamHTScore() {
		return homeTeamHTScore;
	}



	public int getAwayTeamScore() {
		return awayTeamScore;
	}



	public int getAwayTeamHTScore() {
		return awayTeamHTScore;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(new SimpleDateFormat("EEE MMM d yyyy").format(getDate()) + " ");
		sb.append(getDivision() + ": ");
		sb.append(getHomeTeam().getName() + " " + homeTeamScore + ":");
		sb.append(awayTeamScore + " " + getAwayTeam().getName());
		sb.append(" [HT " + homeTeamHTScore + ":" + awayTeamHTScore + "]");
		return sb.toString();
	}

	@Override
	public int compareTo(MatchData otherMatch) {
		
		// compareTo should return < 0 if this is supposed to be
		// less than other, > 0 if this is supposed to be greater than 
		// other and 0 if they are supposed to be equal
		
		if (getDate().before(otherMatch.getDate())) {
			return -1;
		} else if (getDate().after(otherMatch.getDate())) {
			return 1;
		} else {
			// must be the same 
			return 0;
		}
	}
	
}
