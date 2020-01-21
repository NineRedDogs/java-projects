package dogs.red.nine.oracle.gatherer;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.data.Teams;
import dogs.red.nine.oracle.data.tables.FullSeasonTable;
import dogs.red.nine.oracle.data.tables.TableGenerator;
import dogs.red.nine.oracle.general.ResultDataUrlUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.*;

public class GetResults {
	private static final Logger logger = LogManager.getLogger("GetResults");

	private static final String DATA_URLS_FILE_NAME = "dataUrls.txt";
	private static final File FOOTBALL_DATA_URL_FILE = new File(AppConstants.DATA_DIR, DATA_URLS_FILE_NAME);

	private static final String DATA_FILE_COLUMN_KEY_LINE = "Div,";

	private final List<Division> supportedDivisions;
	private final Map<Division, SortedSet<String>> divisionTeams = new HashMap<Division, SortedSet<String>>();
	private final Map<Division, List<MatchData>> allMatches = new HashMap<Division, List<MatchData>>();
	private final TableGenerator tabGen;

	/**
	 * @param supportedDivisions
	 */
	public GetResults(List<Division> supportedDivisions, TableGenerator tableGenerator) {
		this.tabGen = tableGenerator;
		this.supportedDivisions = supportedDivisions;
		for (Division division : supportedDivisions) {
			allMatches.put(division, new ArrayList<MatchData>());
			divisionTeams.put(division, new TreeSet<String>());
		}
	}

	public List<Division> getSupportedDivisions() {
		return supportedDivisions;
	}

	private List<String> setAllFootballDataUrls(final String urlFileName) throws IOException {

		List<String> dataUrls = new ArrayList<String>();
		for (String line : Files.readAllLines(Paths.get(urlFileName), StandardCharsets.UTF_8)) {

			if (line.startsWith("http")) {

				// found a real url, save it
				dataUrls.add(line);
			}
		}
		return dataUrls;
	}

	private File getResultsDataFileFromRemote(final String datafileUrl) {

		final String fname = FilenameUtils.getName(datafileUrl);
		final File localFile = new File(AppConstants.DATA_DIR, fname);

		try {
			logger.debug("getting results from data url : " + datafileUrl);
			InputStream in = new URL(datafileUrl).openStream();
			Files.copy(in, Paths.get(localFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
			logger.debug("set up local file : " + fname);
		} catch (Exception ex) {
			logger.debug("using existing data file (if one exists), as couldnt get file [" + datafileUrl + "] ex: "
					+ ex.getClass().getName() + ", " + ex.getLocalizedMessage());
		}

		return localFile;
	}

	public Teams getResultsFromDataUrls() throws IOException {

		// get result data from data source
		readResultsFromDataUrls();

		// determine game week numbers for matches
		assignGameWeekToMatches();

		// enrich result data with league positions prior to game
		assignLeaguePositionsToMatches();

		// enrich result data further with form indicators prior to game
		assignFormPositionsToMatches();

		return generateTeamsFromMatches();
	}

	private Teams generateTeamsFromMatches() {
		Teams teams = new Teams();
		for (Division division : supportedDivisions) {
			for (MatchData match : allMatches.get(division)) {
				teams.addMatchResult(match);
			}
		}
		teams.updateStats();
		return teams;
	}

	private void assignGameWeekToMatches() {
	}

	private void assignFormPositionsToMatches() {

		// use the following pattern for the creation of all form tables - can even use it for the full table:
		//
		// loop through the matches - gather names of all teams, then
		// filter matches into a new map for a given team
		// then remove any matches that are outside the form range (none removed for the full table)
		// may also keep a home-form table, or home-full-season table - where we filter out away matches for our team.
		//
		// this approach will leave a set of match results that we can then use to create a form table
		// will need to make sure only the given i.e. filtered team is used.
		//
		//
	}

	private void assignLeaguePositionsToMatches() {
		for (Division division : getSupportedDivisions()) {
			tabGen.generateTables(division, allMatches.get(division), divisionTeams.get(division));
			tabGen.displayCurrentTables();
		}
	}

	private void generateTables(Division division) {
		Date currDate = allMatches.get(division).get(0).getDate();
		FullSeasonTable fullSeasonTable;

		for (MatchData match : allMatches.get(division)) {



		}

		
	}

	private void readResultsFromDataUrls() throws IOException {

		for (Division division : getSupportedDivisions()) {
			readDivisionResults(division);
		}
	}

	private void readDivisionResults(final Division division) throws IOException {

		final String divisionResultsUrl = ResultDataUrlUtils.generateResultUrl(division);
		final File dataFile = getResultsDataFileFromRemote(divisionResultsUrl);
		String[] keyData = null;
		String lineReadFromDataFile;

		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			while ((lineReadFromDataFile = in.readLine()) != null) {
				if (lineReadFromDataFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
					String keyLine = lineReadFromDataFile;
					keyData = keyLine.split(",", -1);
				} else {
					MatchData match;
					try {
						match = new MatchData(lineReadFromDataFile, keyData);
						// logger.debug("Parsed match data : " + match);

						// generate a list of all teams in this division
						divisionTeams.get(division).add(match.getHomeTeam());
						divisionTeams.get(division).add(match.getAwayTeam());

						if (supportedDivisions.contains(match.getDivision())) {
							// logger.debug("Match: " + match);
							allMatches.get(division).add(match);
						}
					} catch (ParseException | NumberFormatException e) {
						logger.debug("Parse problem with result : " + lineReadFromDataFile + ", e:" + e.getMessage());
					}
				}
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("Division <" + division + "> :teams::" + divisionTeams.get(division));
	}



	// public void readDivisionResultsFromDataUrl(final Division division) throws IOException {
	// 	final String divisionResultsUrl = ResultDataUrlUtils.generateResultUrl(division);
	// 	final File dataFile = getResultsDataFileFromRemote(divisionResultsUrl);
	// 	String[] keyData = null;
	// 	String lineReadFromDataFile;

	// 	BufferedReader in;
	// 	try {
	// 		in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
	// 		while ((lineReadFromDataFile = in.readLine()) != null) {
	// 			if (lineReadFromDataFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
	// 				String keyLine = lineReadFromDataFile;
	// 				keyData = keyLine.split(",", -1);
	// 			} else {
	// 				MatchData match;
	// 				try {
	// 					match = new MatchData(lineReadFromDataFile, keyData);
	// 					// logger.debug("Parsed match data : " + match);

	// 					if (supportedDivisions.contains(match.getDivision())) {
	// 						// logger.debug("Match: " + match);
	// 						allMatches.add(match);
	// 					}
	// 				} catch (ParseException | NumberFormatException e) {
	// 					logger.debug("Parse problem with result : " + lineReadFromDataFile + ", e:" + e.getMessage());
	// 				}
	// 			}
	// 		}
	// 		in.close();

	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }

}
