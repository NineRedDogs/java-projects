package ajg.adid.data;

public class RegNumGenerator extends BaseGenerator {
	
	private static final String digits = "0123456789";
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int NUM_ABI_CHARS = 14;
		
	public RegNumGenerator() {
	}
	
	private String generateRegNum() {
		StringBuilder sb = new StringBuilder();
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		sb.append(digits.charAt(rnd.nextInt(digits.length())));
		sb.append(digits.charAt(rnd.nextInt(digits.length())));
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		sb.append(letters.charAt(rnd.nextInt(letters.length())));
		return sb.toString();
	}
	
	private String generateAbiCode() {
		StringBuilder sb = new StringBuilder("abi");
		
		while (sb.length() < NUM_ABI_CHARS) {
			sb.append(digits.charAt(rnd.nextInt(digits.length())));
		}
		return sb.toString();
	}
	
	public VehicleInfo generateVehicleData() {
		return new VehicleInfo(generateRegNum(), generateAbiCode());
		
	}

	public static void main(String[] args) {
		RegNumGenerator rg = new RegNumGenerator();
		
		for (int i = 0; i < 9; i++) {
			System.out.println(rg.generateVehicleData());
		}
	}

}
