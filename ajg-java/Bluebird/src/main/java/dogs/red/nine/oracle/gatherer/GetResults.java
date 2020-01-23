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

	/**
	 * @param supportedDivisions
	 */
	public GetResults(List<Division> supportedDivisions) {
		this.supportedDivisions = supportedDivisions;
	}

	private List<Division> getSupportedDivisions() {
		return supportedDivisions;
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

	public Map<Division, List<MatchData>> getResultsFromDataUrls() throws IOException {
		final Map<Division, List<MatchData>> allMatches = new HashMap<Division, List<MatchData>>();

		for (Division division : getSupportedDivisions()) {
			allMatches.put(division, readDivisionResults(division));
		}
		return allMatches;
	}

	private List<MatchData> readDivisionResults(final Division division) throws IOException {
		final List<MatchData> divisionalResults = new ArrayList<MatchData>();
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

						if (supportedDivisions.contains(match.getDivision())) {
//							logger.debug("Div [" + division + "] -- Adding Match: " + match);
							divisionalResults.add(match);
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
		return divisionalResults;
	}
}