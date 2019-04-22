package ajg.adid.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DobGenerator extends BaseGenerator {
	
	public DobGenerator() {
	}
	
	public String generateDob() {
		Random random = new Random();
		int minDay = (int) LocalDate.of(1935, 1, 1).toEpochDay();
		int maxDay = (int) LocalDate.of(2002, 1, 1).toEpochDay();
		long randomDay = minDay + random.nextInt(maxDay - minDay);

		LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
		return randomBirthDate.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
	}
	
	public static void main(String[] args) {
		DobGenerator dg = new DobGenerator();
		
		for (int i = 0; i < 99; i++) {
			System.out.println(dg.generateDob());
		}

	}

}
