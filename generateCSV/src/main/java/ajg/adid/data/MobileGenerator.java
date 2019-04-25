package ajg.adid.data;

import java.util.HashMap;
import java.util.Map;

public class MobileGenerator extends BaseGenerator {
	
	
	private static final String MOBILE_PREFIX = "07";
	private static final int NUM_DIGITS = 11;
	private static final String digits = "0123456789";
    private final Map<String,Boolean> mobiles = new HashMap<String, Boolean>();
	
	public MobileGenerator() {
	}
	
    public String generateMobile() {
        String mobile=""; 
        boolean uniqueMobileNumber = false;
        while (!uniqueMobileNumber) {
            mobile = generateMobileCore();
            if (!mobiles.containsKey(mobile)) {
                uniqueMobileNumber = true;
                mobiles.put(mobile, true);
            }
        }
        return mobile;
    }


	private String generateMobileCore() {
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
