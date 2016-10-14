package dogs.red.nine.footy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class GetResults {
	private static final String FOOTBALL_DATA_URL_FILE="/home/agrahame/Dropbox/workspace/FootballRest/data/dataUrls.txt";
	private static final String DATA_FILE_COLUMN_KEY_LINE = "Div,";
	
	private final List<Division> supportedDivisions;

	/**
	 * @param supportedDivisions
	 */
	public GetResults(List<Division> supportedDivisions) {
		this.supportedDivisions = supportedDivisions;
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
	
	
	public void getResultsFromDataUrls(final Teams teams) throws IOException {
		List<String> dataUrls = setAllFootballDataUrls(FOOTBALL_DATA_URL_FILE);
		
		for (String dataUrl : dataUrls) {
			String[] keyData=null;
			
			URL url = null;
			String lineReadFromDataFile;
			
			try {
			    url = new URL(dataUrl);
			} catch (MalformedURLException e) {
			    e.printStackTrace();
			}
			BufferedReader in;
			try {
			    URLConnection con = url.openConnection();
			    con.setReadTimeout( 1000 ); //1 second
			    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    while ((lineReadFromDataFile = in.readLine()) != null) {
			    	if (lineReadFromDataFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
			    		String keyLine = lineReadFromDataFile;
			    		keyData = keyLine.split(",");
			    	} else {
			    		MatchData match;
						try {
							match = new MatchData(lineReadFromDataFile, keyData);
							//System.out.println("Parsed match data : " + match);

							if (supportedDivisions.contains(match.getDivision())) {
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
	}
}
