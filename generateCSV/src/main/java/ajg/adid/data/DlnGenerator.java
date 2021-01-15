package ajg.adid.data;

import java.util.HashMap;
import java.util.Map;

public class DlnGenerator extends BaseGenerator {
	private static final int DLN_NAME_CHARS = 7;
	private static final int MAX_DLN_ID = 99999999;
    private final Map<String,Boolean> dlns = new HashMap<String, Boolean>();

	public DlnGenerator() {
	}
	
    public String generateDLN(final String surname) {
        String dln=""; 
        boolean uniqueDln = false;
        while (!uniqueDln) {
            dln = generateDlnCore(surname);
            if (!dlns.containsKey(dln)) {
                uniqueDln = true;
                dlns.put(dln, true);
            }
        }
        return dln;
    }

	private String generateDlnCore(final String surname) {
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
