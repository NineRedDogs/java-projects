package dogs.red.nine.oracle.gatherer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dogs.red.nine.oracle.data.Division;
import dogs.red.nine.oracle.data.FixtureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetFixtures {
	private static final Logger logger = LogManager.getLogger("GetFixtures");


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

		if (Gatherer.DEV_MODE) {
			// in dev mode the fixtures will not be available - unless developing after friday 17:00 :)
			// so read in a sample fixtures file
//			url = Paths.get(Gatherer.SAMPLE_FIXTURE_DEV_MODE_FILE).toUri().toURL();
			url = Gatherer.SAMPLE_FIXTURE_DEV_MODE_FILE.toURI().toURL();
			logger.debug("#################################################");
			logger.debug("##### getfixtures : devMode #####################");
			logger.debug("#################################################");

		} else {
			// Real fixtures to read in , lets get some forecasts !!!!!!
			try {
				url = new URL(FOOTBALL_FIXTURES_URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		String[] keyData=null;
		String lineReadFromFixturesFile;

		BufferedReader in;
		try {
			URLConnection con = url.openConnection();
			con.setReadTimeout( 1000 ); //1 second
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((lineReadFromFixturesFile = in.readLine()) != null) {
				if (lineReadFromFixturesFile.startsWith(DATA_FILE_COLUMN_KEY_LINE)) {
					String keyLine = lineReadFromFixturesFile;
					keyData = keyLine.split("," , -1);
					System.out.println("Num columns : " + keyData.length);
					System.out.println(" --- ");
					//Div,Date,Time,HomeTeam,AwayTeam,FTHG,FTAG,FTR,HTHG,HTAG,HTR,B365H,B365D,B365A,BWH,BWD,BWA,IWH,IWD,IWA,PSH,PSD,PSA,WHH,WHD,WHA,VCH,VCD,VCA,MaxH,MaxD,MaxA,AvgH,AvgD,AvgA,B365>2.5,B365<2.5,P>2.5,P<2.5,Max>2.5,Max<2.5,Avg>2.5,Avg<2.5,AHh,B365AHH,B365AHA,PAHH,PAHA,MaxAHH,MaxAHA,AvgAHH,AvgAHA,B365CH,B365CD,B365CA,BWCH,BWCD,BWCA,IWCH,IWCD,IWCA,PSCH,PSCD,PSCA,WHCH,WHCD,WHCA,VCCH,VCCD,VCCA,MaxCH,MaxCD,MaxCA,AvgCH,AvgCD,AvgCA,B365C>2.5,B365C<2.5,PC>2.5,PC<2.5,MaxC>2.5,MaxC<2.5,AvgC>2.5,AvgC<2.5,AHCh,B365CAHH,B365CAHA,PCAHH,PCAHA,MaxCAHH,MaxCAHA,AvgCAHH,AvgCAHA
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
