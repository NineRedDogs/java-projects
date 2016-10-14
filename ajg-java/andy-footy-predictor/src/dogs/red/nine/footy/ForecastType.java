package dogs.red.nine.footy;

import java.util.Set;
import java.util.TreeSet;

public abstract class ForecastType {
	
	Set<FixtureData> fixtures = new TreeSet<FixtureData>();

	public abstract void process(FixtureData fd);
	

}
