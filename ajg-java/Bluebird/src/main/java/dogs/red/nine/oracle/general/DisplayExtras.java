package dogs.red.nine.oracle.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum DisplayExtras {

	PlainText("plain"),
	HighlightHomeTeam("hlh"),
	HighlightAwayTeam("hla");

	private static final Logger logger = LogManager.getLogger("DisplayExtras");


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

	public static String dumpStack(final String ctx) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		String stackStr="";
		int ix=0;
		for (StackTraceElement stackItem : stack) {
			stackStr += "____" + stackItem;
			ix++;
			if (ix > 20) {
				break;
			}
		}
		return new String (ctx + ": called from : " + stackStr);
	}
}
