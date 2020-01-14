package dogs.red.nine.oracle.data;

public enum MatchResult {
	
	  Home_Win("H"),
	  Draw("D"),
	  Away_Win("A");
	  
	  private String resultText;
	     
	private MatchResult(final String resultText) {
		this.resultText = resultText;
	}

	public String getResultText() {
		return resultText;
	}

	public static MatchResult fromString(String text) {
		if (text != null) {
			for (MatchResult b : MatchResult.values()) {
				if (text.equalsIgnoreCase(b.resultText)) {
					return b;
				}
			}
		}
		return null;
	}
	
}
