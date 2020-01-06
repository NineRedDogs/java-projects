package dogs.red.nine.oracle.gatherer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.MatchData;
import dogs.red.nine.oracle.data.Teams;
import dogs.red.nine.oracle.general.ResultDataUrlUtils;


public class GetResults {
	private static final Logger logger = LogManager.getLogger("GetResults");

	private static final String DATA_URLS_FILE_NAME = "dataUrls.txt";
	private static final File FOOTBALL_DATA_URL_FILE = new File(Gatherer.DATA_DIR, DATA_URLS_FILE_NAME);
	
	private static final String DATA_FILE_COLUMN_KEY_LINE = "Div,";
	
	private final List<Division> supportedDivisions;

	/**
	 * @param supportedDivisions
	 */
	public GetResults(List<Division> supportedDivisions) {
		this.supportedDivisions = supportedDivisions;
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

	private File getResultsDataFilesFromRemote(final String datafileUrl) {

		final String fname = FilenameUtils.getName(datafileUrl);
		final File localFile = new File(Gatherer.DATA_DIR, fname);

		try {
			InputStream in = new URL(datafileUrl).openStream();
			Files.copy(in, Paths.get(localFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
			logger.debug("set up local file : " + fname);
		}
		catch (Exception ex) {
			logger.debug("using existing data file (if one exists), as couldnt get file [" + datafileUrl + "] ex: " + ex.getClass().getName() + ", " + ex.getLocalizedMessage());
		}

		return localFile;
	}

	
	
	public Teams getResultsFromDataUrls() throws IOException {
		Teams teams = new Teams();
		
		for (Division division : getSupportedDivisions()) {
			final String divisionResultsUrl = ResultDataUrlUtils.generateResultUrl(division);

			logger.debug("getting results from data url : " + divisionResultsUrl);

			final File dataFile = getResultsDataFilesFromRemote(divisionResultsUrl);

			String[] keyData=null;
			
			// URL url = null;
			String lineReadFromDataFile;
			
			// try {
			//     url = new URL(divisionResultsUrl);
			// } catch (MalformedURLException e) {
			//     e.printStackTrace();
			// }
			BufferedReader in;
			try {
			    // URLConnection con = url.openConnection();
			    // con.setReadTimeout( 1000 ); //1 second
			    // in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			    while ((lineReadFromDataFile = in.readLine()) != null) {
			    	if (lineReadFromDataFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
			    		String keyLine = lineReadFromDataFile;
			    		keyData = keyLine.split("," , -1);
			    	} else {
			    		MatchData match;
						try {
							match = new MatchData(lineReadFromDataFile, keyData);
							//System.out.println("Parsed match data : " + match);

							if (supportedDivisions.contains(match.getDivision())) {
								//logger.debug("Match: " + match);
								teams.addMatchResult(match);
							}
						} catch (ParseException | NumberFormatException e) {
							//System.out.println("Parse problem with result : " + lineReadFromDataFile);
							//e.printStackTrace();
						}
			    	}
			    }
			    in.close();

			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		teams.updateStats();
		return teams;
	}


}
