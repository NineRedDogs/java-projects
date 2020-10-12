package dogs.red.nine.oracle.gatherer;

import dogs.red.nine.oracle.AppConstants;
import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.general.ResultDataUrlUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.*;

public class GetResults {
	private static final Logger logger = LogManager.getLogger("GetResults");

	private static final String DATA_FILE_COLUMN_KEY_LINE = "Div,";

	private final List<Division> supportedDivisions;
	private final String seasonToUse;

	/**
	 * Constructor.
	 *
	 * @param supportedDivisions list of divisions to be used for forecasts
	 * @param season what season to use
	 */
	public GetResults(List<Division> supportedDivisions, String season) {
		this.supportedDivisions = supportedDivisions;
		this.seasonToUse = season;
	}

	private List<Division> getSupportedDivisions() {
			return supportedDivisions;

	}

	public List<Division> getActiveLeagues() {
		return getSupportedDivisions();
	}

	private String showSupportedDivisions() {
		StringBuilder sb = new StringBuilder("Supported Leagues : " + System.lineSeparator());
		for (Division div : getSupportedDivisions()) {
			sb.append("       ").append(div.name()).append(System.lineSeparator());
		}
		return sb.toString();
	}

	private void removeInactiveLeagues(List<Division> inactiveLeagues) {
		if (inactiveLeagues.size() > 0) {
			for (Division division : inactiveLeagues) {
				logger.error("Removing league (season not started yet) : " + division);
				supportedDivisions.remove(division);
			}
			logger.error(System.lineSeparator() + "Updated list of leagues (after removing inactive league) " + showSupportedDivisions());
		}
	}

	private File getResultsDataFileFromRemote(final String datafileUrl) {

		final String fName = FilenameUtils.getName(datafileUrl);
		final File localFile = new File(AppConstants.DATA_DIR, fName);

		try {
			//logger.debug("getting results from data url : " + datafileUrl);
			InputStream in = new URL(datafileUrl).openStream();
			Files.copy(in, Paths.get(localFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
			//logger.debug("set up local file : " + fName);
		} catch (Exception ex) {
			logger.debug("using existing data file (if one exists), as couldn't get file [" + datafileUrl + "] ex: "
					+ ex.getClass().getName() + ", " + ex.getLocalizedMessage());
		}
		return localFile;
	}

	public Map<Division, List<MatchData>> getResultsFromDataUrls() {
		final Map<Division, List<MatchData>> allMatches = new HashMap<>();
		final List<Division> inactiveLeagues = new ArrayList<>();

		for (Division division : getSupportedDivisions()) {
			try {
				allMatches.put(division, readDivisionResults(division));
			} catch (ParseException | IOException e) {
				// problem reading all results for this league, so remove league from list
				inactiveLeagues.add(division);
			}
		}
		removeInactiveLeagues(inactiveLeagues);
		return allMatches;
	}



	private List<MatchData> readDivisionResults(final Division division) throws ParseException, IOException {
		final List<MatchData> divisionalResults = new ArrayList<>();
		final String divisionResultsUrl = ResultDataUrlUtils.generateResultUrl(division, seasonToUse);
		final File dataFile = getResultsDataFileFromRemote(divisionResultsUrl);
		//logger.debug(">>>>> Data url for division : " + division + " is " + divisionResultsUrl);
		String[] keyData = null;
		String lineReadFromDataFile = "";

		BufferedReader in;
		in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
		try {
			while ((lineReadFromDataFile = in.readLine()) != null) {
				if (lineReadFromDataFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
					String keyLine;
					keyLine = lineReadFromDataFile;
					keyData = keyLine.split(",", -1);
				} else {
					MatchData match = new MatchData(lineReadFromDataFile, keyData);
					// logger.debug("Parsed match data : " + match);

					if (supportedDivisions.contains(match.getDivision())) {
//							logger.debug("Div [" + division + "] -- Adding Match: " + match);
						divisionalResults.add(match);
					}
				}
			}
		} catch (ParseException | NumberFormatException e) {
			//logger.error("Parse problem with result : " + lineReadFromDataFile + ", e:" + e.getMessage() +
			//		" Will remove " + division + " from list of Active leagues ...");
			throw e;
		} finally {
			in.close();
		}

		return divisionalResults;
	}
}