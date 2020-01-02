package dogs.red.nine.footy;

import java.util.Arrays;
import java.util.List;

public abstract class HighScorersForecastType extends ForecastType {
	
	// Sky bet doesn't give the option for score 2+ for these leagues, so need to omit from results (if using UK leagues)
    private static final List<Division> UK_DIVISIONS_NOT_FOR_HIGH_SCORES = Arrays.asList(
			Division.Scotland_Championship ,
			Division.Scotland_Div_1 ,
			Division.Scotland_Div_2);

	protected boolean extraRulesForInclusion(final FixtureData fd) {
		if (Forecaster.USE_UK_LEAGUES) {
			if (UK_DIVISIONS_NOT_FOR_HIGH_SCORES.contains(fd.getDivision())) {
				return false;
			}
		}
		return true;
	}

}
