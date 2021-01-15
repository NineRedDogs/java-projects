package ajg.adid.data;

public class Town {
	private final String town;
	private final String county;
	private final String pcPrefix;
	
	public Town(String town, String county, String pc) {
		this.town = town;
		this.county = county;
		this.pcPrefix = pc;
	}
	
	public String getTown() {
		return town;
	}
	
	public String getCounty() {
		return county;
	}
	
	public String getPcPrefix() {
		return pcPrefix;
	}
}
