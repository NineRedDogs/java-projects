package ajg.adid.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Towns extends BaseGenerator {
	
	private static final String TOWNS_FILE = DATA_DIR + "towns";
	private final Town[] towns;
	
	public final int NUM_TOWNS;
	
	public Towns() {
		towns = readTowns(TOWNS_FILE);
		NUM_TOWNS=towns.length;
	}

	
	private Town[] readTowns(String townsFname) {
		//Town[] towns = new ArrayList<Town>();
		ArrayList<Town> towns = new ArrayList<Town>();

		String line = null;

		// Read all lines in from CSV file and add to towns
		FileReader fileReader;
		try {
			fileReader = new FileReader(townsFname);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] splitRow = line.split(",");
				String town = splitRow[0];
				String county = splitRow[1];
				String pcPrefix = splitRow[2];
				towns.add(new Town(town, county, pcPrefix));
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("Town File could not be found [" + townsFname + "]");
		} catch (IOException e) {
			System.out.println("Towns: Caught IOException : " + e.getMessage());
		}
		return towns.toArray(new Town[towns.size()]);
	}
	
	public Town getTown() {
		return towns[rnd.nextInt(NUM_TOWNS)];
	}


}
