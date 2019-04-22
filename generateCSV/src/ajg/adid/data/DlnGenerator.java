package ajg.adid.data;

public class DlnGenerator extends BaseGenerator {
	
	
	private static final int DLN_NAME_CHARS = 7;
	private static final int MAX_DLN_ID = 99999999;
	
	
	public DlnGenerator() {
	}
	
	public String generateDLN(final String surname) {
		String surnameBit = (surname.length() < DLN_NAME_CHARS) ? surname : surname.substring(0, DLN_NAME_CHARS);
		int dlnId = rnd.nextInt(MAX_DLN_ID);
		StringBuilder sb = new StringBuilder(surnameBit);
		sb.append(dlnId);
		return sb.toString();

	}
	
	public static void main(String[] args) {
		DlnGenerator dg = new DlnGenerator();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(dg.generateDLN("grahame"));
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(dg.generateDLN("jones"));
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(dg.generateDLN("washington"));
		}
	}

}
