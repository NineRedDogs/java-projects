package dogs.red.nine.oracle.data;

public enum Division {

	Belgium_Juliper_1("B1"),
	Germany_Bundesliga_1("D1"),
	Germany_Bundesliga_2("D2"),
	England_Premier_League("E0"),
	England_Championship("E1"),
	England_League_1("E2"),
	England_League_2("E3"),
	England_Conference("EC"),
	France_Ligue_1("F1"),
	France_Ligue_2("F2"),
	Greece_Division_1("G1"),
	Italy_Serie_A("I1"),
	Italy_Serie_B("I2"),
	Holland_Eridivisie("N1"),
	Spain_La_Liga("SP1"),
	Spain_League_2("SP2"),
	Scotland_Premier_League("SC0"),
	Scotland_Championship("SC1"),
	Scotland_Div_1("SC2"),
	Scotland_Div_2("SC3"),
	Portugal_Primera("P1"),
	Turkey_Division_1("T1");

	  private String divCode;
	     
	private Division(final String divCode) {
		this.divCode = divCode;
	}

	public String getDivCode() {
		return divCode;
	}

	public static Division fromString(String text) {
		if (text != null) {
			for (Division b : Division.values()) {
				if (text.equalsIgnoreCase(b.divCode)) {
					return b;
				}
			}
		}
		return null;
	}
	
}
