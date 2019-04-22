package ajg.adid.data;

public class VehicleInfo {
	private final String regNum;
	private final String abiCode;
	
	/**
	 * @param regNum
	 * @param abiCode
	 */
	public VehicleInfo(String regNum, String abiCode) {
		super();
		this.regNum = regNum;
		this.abiCode = abiCode;
	}

	/**
	 * @return the regnum
	 */
	public String getRegNum() {
		return regNum;
	}

	/**
	 * @return the abiCode
	 */
	public String getAbiCode() {
		return abiCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VehicleInfo [regNum=" + regNum + ", abiCode=" + abiCode + "]";
	}


}
