package dogs.red.nine.oracle.general;

public enum DisplayExtras {
	
	PlainText("plain"),
	HighlightHomeTeam("hlh"),
	HighlightAwayTeam("hla");
	  
	private String deStr;
	     
	private DisplayExtras(final String deStr) {
		this.deStr = deStr;
	}

	public String getDeStr() {
		return deStr;
	}

	public boolean isPlain() {
		return deStr.equalsIgnoreCase("plain");
	}

	public boolean isHlHome() {
		return deStr.equalsIgnoreCase("hlh");
	}

	public boolean isHlAway() {
		return deStr.equalsIgnoreCase("hla");
	}

	public static DisplayExtras fromString(String deStr) {
		if (deStr != null) {
			for (DisplayExtras de : DisplayExtras.values()) {
				if (deStr.equalsIgnoreCase(de.deStr)) {
					return de;
				}
			}
		}
		return null;
	}
}
