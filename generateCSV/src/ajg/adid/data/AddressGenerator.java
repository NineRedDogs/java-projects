package ajg.adid.data;

public class AddressGenerator extends BaseGenerator {
	
	private static final String STREETS_FILE = DATA_DIR + "streets";
	private static final int MAX_STREET_HOUSE_NUMBER = 999;
	private final String[] streets;
	private final Towns towns;	
	private static final String digits = "0123456789";
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int NUM_ALF_CHARS = 10;
	
	private final int NUM_STREETS;
	
	public AddressGenerator() {
		towns = new Towns();
		streets = readFile(STREETS_FILE);
		NUM_STREETS=streets.length;
	}
	
	public String getStreet() {
		return streets[rnd.nextInt(NUM_STREETS)];
	}
	
	public String getStreetAddress() {
		final int houseNumber = rnd.nextInt(MAX_STREET_HOUSE_NUMBER) + 1;
		return new String(houseNumber + " " + getStreet());
	}
	
	private String postCodeGenerator(final String pcPrefix) {
		StringBuilder sb = new StringBuilder(pcPrefix);
		sb.append(rnd.nextInt(899) + 100);
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		return sb.toString();
	}
	
	
	private String generateAlfKey() {
		StringBuilder sb = new StringBuilder("ALF");
		
		while (sb.length() < NUM_ALF_CHARS) {
			sb.append(digits.charAt(rnd.nextInt(digits.length())));
		}
		return sb.toString();
	}
	
	
	
	public Address getAddress() {
		Town t = towns.getTown();
		return new Address(getStreetAddress(), t.getTown(), t.getCounty(), postCodeGenerator(t.getPcPrefix()), generateAlfKey());
	}



	public static void main(String[] args) {
		AddressGenerator ng = new AddressGenerator();
		
		for (int i = 0; i < 9; i++) {
			System.out.println(ng.getAddress());
		}
	}
	
}
