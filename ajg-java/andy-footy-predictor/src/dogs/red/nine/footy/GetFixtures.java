package dogs.red.nine.footy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GetFixtures {
	// get fixtures from : http://www.football-data.co.uk/matches.php

	// use this URL to get fixtures (after 17:00 Friday - see below) http://www.football-data.co.uk/fixtures.csv
	protected static final String DATA_FILE_COLUMN_KEY_LINE = "Div,";


	/**
	 * Please note that the odds are collected for the downloadable weekend fixtures 
	 * on Fridays afternoons not later than 17:00 British Standard Time. 
	 * 
	 * Odds for midweek fixtures are collected Tuesdays not later than 15:00 British Standard Time.
	 */

	private static final String FOOTBALL_FIXTURES_URL="http://www.football-data.co.uk/fixtures.csv";

	private final List<Division> supportedDivisions;

	public GetFixtures(List<Division> supportedDivisions) {
		this.supportedDivisions = supportedDivisions;
	}

	public List<FixtureData>  getFixtures() throws IOException {
		List<FixtureData> allFixtures = new ArrayList<FixtureData>();

		URL url = null;
		String[] keyData=null;

		String lineReadFromFixturesFile;

		try {
			url = new URL(FOOTBALL_FIXTURES_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		BufferedReader in;
		try {
			URLConnection con = url.openConnection();
			con.setReadTimeout( 1000 ); //1 second
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((lineReadFromFixturesFile = in.readLine()) != null) {
				if (lineReadFromFixturesFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
					String keyLine = lineReadFromFixturesFile;
					keyData = keyLine.split(",");
				} else {

					//System.out.println("fixture : " + lineReadFromFixturesFile);
					FixtureData fixture;
					try {
						fixture = new FixtureData(lineReadFromFixturesFile, keyData);
						//System.out.println("Parsed fixture data : " + fixture.fixturePrint());
						if (supportedDivisions.contains(fixture.getDivision())) {
							//System.out.println("Adding fixture from supported division : " + fixture.getDivision());
							//System.out.println(fixture.fixturePrint());
							allFixtures.add(fixture);
						}
					} catch (ParseException e) {
						//System.out.println("Parse problem with result : " + lineReadFromDataFile);
						//e.printStackTrace();
					}
				}

			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return allFixtures;
	}

}
