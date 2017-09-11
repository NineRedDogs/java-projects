package dogs.red.nine.footy;

public enum HomeOrAway {
	
	  Home("h"),
	  Away("a");
	  
	  private String hoaStr;
	     
	private HomeOrAway(final String hoaStr) {
		this.hoaStr = hoaStr;
	}

	public String getHoAStr() {
		return hoaStr;
	}
	
	public boolean isHome() {
		return hoaStr.equalsIgnoreCase("h");
	}

	public boolean isAway() {
		return hoaStr.equalsIgnoreCase("a");
	}

	public static HomeOrAway fromString(String hoaStr) {
		if (hoaStr != null) {
			for (HomeOrAway b : HomeOrAway.values()) {
				if (hoaStr.equalsIgnoreCase(b.hoaStr)) {
					return b;
				}
			}
		}
		return null;
	}
	
}
