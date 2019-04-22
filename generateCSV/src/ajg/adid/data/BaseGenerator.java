package ajg.adid.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class BaseGenerator {
	
	protected final static String DATA_DIR = System.getProperty("user.dir") + File.separator + "bin/data/";
	
	protected final Random rnd = new Random();
	
	public BaseGenerator() {
	}

	public String[] readFile(final String fName) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fName));

			String line;
			while((line = reader.readLine()) != null) {
			    lines.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found [" + fName + "]");
		} catch (IOException e) {
			System.out.println("Caught IOException : " + e.getMessage());
		}
		return lines.toArray(new String[lines.size()]);

	}
	
}
