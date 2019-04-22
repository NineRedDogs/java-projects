package ajg.adid.data;

public class CsvRow {
	
	private static final int MAKE_RANDOM_DELETE_FIELD = 9999;
	private static final int MAKE_RANDOM_MODIFY_FIELD = 19999;
	private final String rowId;
	private final String policyType;
	private final String policyNum;
	private final String forename;
	private final String surname;
	private final String addr1;
	private final String addr2;
	private final String addr3;
	private final String postcode;
	private final String dob;
	private final String mobile;
	private final String email;
	private final String regNum;
	private final String dln;
	private final String deviceId;
	private final String abiCode;
	private final String alfKey;
	private final String dateInc;
	private final String dateExp;
	private final String dateOrin;
	private final String dateCanc;
	
	private final Chance chance= new Chance();
	
	

	/**
	 * @param rowId
	 * @param policyType
	 * @param policyNum
	 * @param forename
	 * @param surname
	 * @param addr1
	 * @param addr2
	 * @param addr3
	 * @param postcode
	 * @param dob
	 * @param mobile
	 * @param email
	 * @param regNum
	 * @param dln
	 * @param deviceId
	 * @param abiCode
	 * @param alfKey
	 * @param dateInc
	 * @param dateExp
	 * @param dateOrin
	 * @param dateCanc
	 */
	public CsvRow(String rowId, String policyType, String policyNum, String forename, String surname, String addr1,
			String addr2, String addr3, String postcode, String dob, String mobile, String email, String regNum,
			String dln, String deviceId, String abiCode, String alfKey, String dateInc, String dateExp, String dateOrin,
			String dateCanc) {
		super();
		this.rowId = rowId;
		this.policyType = policyType;
		this.policyNum = getValueNoDelete(policyNum);
		this.forename = getValueNoDelete(forename);
		this.surname = getValueNoDelete(surname);
		this.addr1 = getValueNoDelete(addr1);
		this.addr2 = getValue(addr2);
		this.addr3 = getValue(addr3);
		this.postcode = getValue(postcode);
		this.dob = getDate(dob);
		this.mobile = getValue(mobile);
		this.email = getValue(email);
		this.regNum = getValue(regNum);
		this.dln = getValue(dln);
		this.deviceId = getValue(deviceId);
		this.abiCode = getValue(abiCode);
		this.alfKey = getValue(alfKey);
		this.dateInc = getDate(dateInc);
		this.dateExp = getDate(dateExp);
		this.dateOrin = getDate(dateOrin);
		this.dateCanc = getDate(dateCanc);
	}
	
	private String getDate(String origDate) {
		if (chance.feelingLuckyPunk(MAKE_RANDOM_MODIFY_FIELD)) {
			// nothing fancy, just set to a fixed date
			return "01/01/1970";
		} else if (chance.feelingLuckyPunk(MAKE_RANDOM_DELETE_FIELD)) {
			return "";	
		}
		// no changes, just use the original date (for most cases)
		return origDate;
	}
	
	private String getValue(String origValue) {
		if (chance.feelingLuckyPunk(MAKE_RANDOM_MODIFY_FIELD)) {
			return modText(origValue);
		} else if (chance.feelingLuckyPunk(MAKE_RANDOM_DELETE_FIELD)) {
			return "";	
		}
		// no changes, just use the original date (for most cases)
		return origValue;
	}
	
	private String getValueNoDelete(String origValue) {
		if (chance.feelingLuckyPunk(MAKE_RANDOM_MODIFY_FIELD)) {
			return modText(origValue);
		} 
		// no changes, just use the original date (for most cases)
		return origValue;
	}
	
	private String modText(String origStr) {
		// to modify just add some text before and after and simply return....
		StringBuilder sb = new StringBuilder("XX_");
		sb.append(origStr);
		sb.append("_XX");
		return sb.toString();
	}
	
	/**
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}
	/**
	 * @return the policyType
	 */
	public String getPolicyType() {
		return policyType;
	}
	/**
	 * @return the policyNum
	 */
	public String getPolicyNum() {
		return policyNum;
	}
	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @return the addr1
	 */
	public String getAddr1() {
		return addr1;
	}
	/**
	 * @return the addr2
	 */
	public String getAddr2() {
		return addr2;
	}
	/**
	 * @return the addr3
	 */
	public String getAddr3() {
		return addr3;
	}
	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the regNum
	 */
	public String getRegNum() {
		return regNum;
	}
	/**
	 * @return the dln
	 */
	public String getDln() {
		return dln;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @return the abiCode
	 */
	public String getAbiCode() {
		return abiCode;
	}
	/**
	 * @return the alfKey
	 */
	public String getAlfKey() {
		return alfKey;
	}
	/**
	 * @return the dateInc
	 */
	public String getDateInc() {
		return dateInc;
	}
	/**
	 * @return the dateExp
	 */
	public String getDateExp() {
		return dateExp;
	}
	/**
	 * @return the dateOrin
	 */
	public String getDateOrin() {
		return dateOrin;
	}
	/**
	 * @return the dateCanc
	 */
	public String getDateCanc() {
		return dateCanc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return rowId + "," + policyType + "," + policyNum + ","
				+ forename + "," + surname + "," + addr1 + "," + addr2 + "," + addr3
				+ "," + postcode + "," + dob + "," + mobile + "," + email + ","
				+ regNum + "," + dln + "," + deviceId + "," + abiCode + "," + alfKey
				+ "," + dateInc + "," + dateExp + "," + dateOrin + "," + dateCanc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String headers() {
		return "rowId,policyType, policyNum,forename,surname,addr1,addr2,addr3,postcode,dob,mobile,email,regNum,dln,deviceId,abiCode,alfKey,dateInc,dateExp,dateOrin,dateCanc";
	}
	

}
