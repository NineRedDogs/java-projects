package ajg.adid.data;

import java.util.Arrays;
import java.util.List;

public class AdmiralPolicy {
	private final String product;
	private final String policyNumber;
	private final String dateInception;
	private final String dateExpired;
	private final String dateOriginated;
	private final String dateCancelled;
	
	/**
	 * @param product
	 * @param policyNumber
	 * @param dateInception
	 * @param dateExpired
	 * @param dateOriginated
	 * @param dateCancelled
	 */
	public AdmiralPolicy(String product, String policyNumber, String dateInception, String dateExpired,
			String dateOriginated, String dateCancelled) {
		super();
		this.product = product;
		this.policyNumber = policyNumber;
		this.dateInception = dateInception;
		this.dateExpired = dateExpired;
		this.dateOriginated = dateOriginated;
		this.dateCancelled = dateCancelled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdmiralPolicy [product=" + product + ", policyNumber=" + policyNumber + ", dateInception="
				+ dateInception + ", dateExpired=" + dateExpired + ", dateOriginated=" + dateOriginated
				+ ", dateCancelled=" + dateCancelled + "]";
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @return the policyNumber
	 */
	public String getPolicyNumber() {
		return policyNumber;
	}

	/**
	 * @return the dateInception
	 */
	public String getDateInception() {
		return dateInception;
	}

	/**
	 * @return the dateExpired
	 */
	public String getDateExpired() {
		return dateExpired;
	}

	/**
	 * @return the dateOriginated
	 */
	public String getDateOriginated() {
		return dateOriginated;
	}

	/**
	 * @return the dateCancelled
	 */
	public String getDateCancelled() {
		return dateCancelled;
	}

	public boolean isMotor() {
		List<String> myList = Arrays.asList("motor", "mp", "van");		
		return myList.contains(product.toLowerCase());
	}


	

}
