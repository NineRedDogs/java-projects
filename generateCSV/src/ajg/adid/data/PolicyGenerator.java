package ajg.adid.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PolicyGenerator extends BaseGenerator {
	
	
	private static final String PRODUCTS_FILE = DATA_DIR + "products";
	private final String[] admiralProducts;
	private static final int NUM_DIGITS = 12;
	private static final String digits = "0123456789";
	private static final int POLICY_PREFIX_CHARS = 3;
	
	private final int NUM_PRODUCTS;
	
	public PolicyGenerator() {
		admiralProducts = readFile(PRODUCTS_FILE);
		NUM_PRODUCTS=admiralProducts.length;
	}
	
	public AdmiralPolicy generatePolicy() {
		final String product = admiralProducts[rnd.nextInt(NUM_PRODUCTS)];
		final String policyNumber = generatePolicyNumber(product);
		
		return new AdmiralPolicy(product, policyNumber, generatePolicyDate(), generatePolicyDate(), generatePolicyDate(), generatePolicyDate());
	}

	public int getNumProducts() {
		return NUM_PRODUCTS;
	}
	
	private String generatePolicyNumber(final String product) {
		final String prefix = product.length() < POLICY_PREFIX_CHARS ? product : product.substring(0, POLICY_PREFIX_CHARS);
		StringBuilder sb = new StringBuilder(prefix.toUpperCase());
		
		while (sb.length() < NUM_DIGITS) {
			sb.append(digits.charAt(rnd.nextInt(digits.length())));
		}
		return sb.toString();

	}
	
	public String generatePolicyDate() {
		Random random = new Random();
		int minDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
		int maxDay = (int) LocalDate.now().toEpochDay();
		long randomDay = minDay + random.nextInt(maxDay - minDay);

		LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
		return randomBirthDate.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
	}
	
	public static void main(String[] args) {
		PolicyGenerator pg = new PolicyGenerator();
		
		for (int i = 0; i < 99; i++) {
			System.out.println(pg.generatePolicy());
		}
	}

}
