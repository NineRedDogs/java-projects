package ajg.adid.data;

public class Address {
	
	private final String address1;
	private final String address2;
	private final String address3;
	private final String postcode;
	private final String alfKey;

	/**
	 * @param address1
	 * @param address2
	 * @param address3
	 * @param postcode
	 * @param alfKey
	 */
	public Address(String address1, String address2, String address3, String postcode, String alfKey) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.postcode = postcode;
		this.alfKey = alfKey;
	}


	public String getAddress1() {
		return address1;
	}


	public String getAddress2() {
		return address2;
	}


	public String getAddress3() {
		return address3;
	}


	public String getPostcode() {
		return postcode;
	}


	/**
	 * @return the alfKey
	 */
	public String getAlfKey() {
		return alfKey;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Address [" + address1 + ", " + address2 + ", " + address3 + "\npostcode="
				+ postcode + ", alfKey=" + alfKey + "]";
	}




}
