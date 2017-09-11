package dogs.red.nine.footy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Results {
	
	private static final Logger logger = LogManager.getLogger("Results");

	private List<MatchData> results;
	private List<MatchData> homeResults;
	private List<MatchData> awayResults;
	private final String teamName;
	

	public String getTeamName() {
		return teamName;
	}
	public Results(String name) {
		this.teamName = name;
		results = new ArrayList<MatchData>();
		homeResults = new ArrayList<MatchData>();
		awayResults = new ArrayList<MatchData>();
	}
	public void addResult(MatchData resultData) {
		results.add(resultData);
		if (resultData.getHomeTeam().getName().equalsIgnoreCase(teamName)) {
			homeResults.add(resultData);
		} else {
			awayResults.add(resultData);
		}
	}
	
	public List<MatchData> getResults() {
		return results;
	}
	
	/**
	 * Traverses all the results and creates a set of home results and a set of away results - sorted by date
	 */
	public void organise() {
		
		
	}
	
	
	public void trim(int formMatches) {
		Collections.reverse(results);
		Collections.reverse(homeResults);
		Collections.reverse(awayResults);

		// half the total-form number for num home/away matches to use for form
		int homeAwayMatches = formMatches/2;
		
		// only trim to num form matches if there are more than enough matches
		if (results.size() > formMatches) {
			results = new ArrayList<MatchData>(results.subList(0, formMatches));
		}
		
		// only trim to num form matches if there are more than enough matches
		if (homeResults.size() > homeAwayMatches) {
			homeResults = new ArrayList<MatchData>(homeResults.subList(0, homeAwayMatches));
		}

		// only trim to num form matches if there are more than enough matches
		if (awayResults.size() > homeAwayMatches) {
			awayResults = new ArrayList<MatchData>(awayResults.subList(0, homeAwayMatches));
		}
	}
	
	public void dumpList(final String desc) {
		logger.info("---------------------------------------");
		logger.info(desc);
		logger.info("---------------------------------------");
		logger.info("All results ");
		for (MatchData matchData : results) {
			logger.info(matchData);
			
		}
		logger.info("---------------------------------------");
		logger.info("Home results ");
		for (MatchData matchData : homeResults) {
			logger.info(matchData);
			
		}
		logger.info("---------------------------------------");
		logger.info("Away results ");
		for (MatchData matchData : awayResults) {
			logger.info(matchData);
			
		}
		logger.info("---------------------------------------");
	}


}
