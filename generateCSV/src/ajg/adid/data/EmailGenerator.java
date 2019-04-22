package ajg.adid.data;

public class EmailGenerator extends BaseGenerator {
	
	
	private static final String PROVIDERS_FILE = DATA_DIR + "email-providers";
	private static final int FIRST_LETTER = 0;
	private final String[] emailProviders;
	private final Chance c = new Chance();
	
	private final int NUM_PROVIDERS;
	
	public EmailGenerator() {
		emailProviders = readFile(PROVIDERS_FILE);
		NUM_PROVIDERS=emailProviders.length;
	}
	
	public String generateEmailAddress(final String forename, final String surname) {
		final String forenameBit = genForenamePortion(forename);
		String emailAddress;
		
		if (c.feelingLuckyPunk(500)) {
			// one in 500 to have their own domain
			emailAddress = forenameBit + "@" + surname + ".com";
		} else {
			StringBuilder sb = new StringBuilder(forenameBit);
			if (c.feelingLuckyPunk(3)) {
				sb.append("_");
			}
			sb.append(surname);
			if (c.feelingLuckyPunk(2)) {
				sb.append(rnd.nextInt(999));
			}
			sb.append("@");
			sb.append(emailProviders[rnd.nextInt(NUM_PROVIDERS)]);
			emailAddress = sb.toString();
		}
		return emailAddress;
	}
	
	private String genForenamePortion(String forename) {
		final StringBuilder sb = new StringBuilder();
		if (c.feelingLuckyPunk(3)) {
			// one in 3 chance to just use initial in email
			sb.append(forename.charAt(FIRST_LETTER));
		} else {
			sb.append(forename);
		}
		return sb.toString();
	}


	
	
	public static void main(String[] args) {
		EmailGenerator eg = new EmailGenerator();
		
		for (int i = 0; i < 99; i++) {
			System.out.println(eg.generateEmailAddress("andrew", "grahame"));
		}
	}

}
