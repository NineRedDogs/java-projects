package ajg.adid.data;

public class NameGenerator extends BaseGenerator {
	
	
	private static final String FORENAMES_FILE = DATA_DIR + "forenames";
	private static final String SURNAMES_FILE = DATA_DIR + "surnames";
	private final String[] forenames;
	private final String[] surnames;
	
	private final int NUM_FORENAMES;
	private final int NUM_SURNAMES;
	
	public NameGenerator() {
		forenames = readFile(FORENAMES_FILE);
		surnames = readFile(SURNAMES_FILE);
		NUM_FORENAMES=forenames.length;
		NUM_SURNAMES=surnames.length;

	}
	
	public String getForename() {
		return forenames[rnd.nextInt(NUM_FORENAMES)];
	}
	
	public String getSurname() {
		return surnames[rnd.nextInt(NUM_SURNAMES)];
	}
	
	public String generateName() {
		return getForename() + " " + getSurname();
	}
	
	
	
	public static void main(String[] args) {
		NameGenerator ng = new NameGenerator();
		
		for (int i = 0; i < 9999999; i++) {
			System.out.println(ng.generateName());
		}
	}

}
