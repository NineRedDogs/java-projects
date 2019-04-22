package ajg.adid.data;

public class MobileGenerator extends BaseGenerator {
	
	
	private static final String MOBILE_PREFIX = "07";
	private static final int NUM_DIGITS = 11;
	private static final String digits = "0123456789";
	
	public MobileGenerator() {
	}
	
	public String generateMobile() {
		StringBuilder sb = new StringBuilder(MOBILE_PREFIX);
		
		while (sb.length() < NUM_DIGITS) {
			sb.append(digits.charAt(rnd.nextInt(digits.length())));
		}
		return sb.toString();

	}
	
	public static void main(String[] args) {
		MobileGenerator mg = new MobileGenerator();
		
		for (int i = 0; i < 9; i++) {
			System.out.println(mg.generateMobile());
		}

	}

}
