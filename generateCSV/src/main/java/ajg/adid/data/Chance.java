package ajg.adid.data;

import java.util.Random;

public class Chance {
	protected final Random rnd = new Random();
	
	public Chance() {
	}
	
	public boolean feelingLuckyPunk(final int oneIn) {
		return (rnd.nextInt(oneIn) == 0);
	}

	
	public static void main(String[] args) {
		Chance c = new Chance();
		final int oneIn=999;
		int iteration=0;
		while (!c.feelingLuckyPunk(oneIn)) {
			iteration++;	
		}
		System.out.println("Got lucky with 1 in " + oneIn + " on iteration " + iteration);
	}
}
