package dogs.red.nine.footy;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Team {
	
	private static final Logger logger = LogManager.getLogger("Team");

	final private String name;
	
	static final int NUM_GAMES_TO_BE_USED_FOR_CURRENT_FORM = 10;
	
	static final boolean USE_FORM_STATS = true;
	
	private Results results;
	
	private TeamStats statsSeason;
	private TeamStats statsForm;
	
	/**
	 * @param name
	 */
	public Team(String name) {
		super();
		this.name = name;
		results = new Results(name);
		statsSeason  = new TeamStats(name);
		statsForm = new TeamStats(name, NUM_GAMES_TO_BE_USED_FOR_CURRENT_FORM);
	}

	public String getName(boolean highlightName) {
		if (highlightName) {
			return "**(" + name.toUpperCase() + ")**";
		} else {
			return name;
		}
	}

	public String getName() {
		return getName(false);
	}
	
	public TeamStats getSeasonStats() {
		return statsSeason;
	}

	public TeamStats getFormStats() {
		return statsForm;
	}
	
	public void addResult(final MatchData resultData) {
		//System.out.println("team : " + name + " adding result : " + resultData.fixturePrint());
		results.addResult(resultData);
		//System.out.println(name + " now has " + results.size() + " results");
	}
	
	public void updateStats() {
		results.organise();
		statsSeason.calcStats(results);	
		//log.info("Team : " + name + " totalStats: " + statsSeason.getTotalStats());
		statsForm.calcStats(results);	
		//log.info("Team : " + name + " formStats: " + statsForm.getTotalStats());
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	public List<MatchData> getResults() {
		return results.getResults();
	}

	public int getToScoreHomeRating() {
		if (USE_FORM_STATS) {
			return statsForm.getToScoreHomeRating();
		} else {
			return statsSeason.getToScoreHomeRating();
		}
	}

	public int getToScoreHomeRatingX() {
		if (USE_FORM_STATS) {
			return statsForm.getToScoreHomeRatingX();
		} else {
			return statsSeason.getToScoreHomeRatingX();
		}
	}

	public int getToScoreAwayRatingX() {
		if (USE_FORM_STATS) {
			return statsForm.getToScoreAwayRatingX();
		} else {
			return statsSeason.getToScoreAwayRatingX();
		}
	}

	public int getToConcedeAwayRating() {
		if (USE_FORM_STATS) {
			return statsForm.getToConcedeAwayRating();
		} else {
			return statsSeason.getToConcedeAwayRating();
		}
	}

	public int getToConcedeHomeRatingX() {
		if (USE_FORM_STATS) {
			return statsForm.getToConcedeHomeRatingX();
		} else {
			return statsSeason.getToConcedeHomeRatingX();
		}
	}

	public int getToConcedeAwayRatingX() {
		if (USE_FORM_STATS) {
			return statsForm.getToConcedeAwayRatingX();
		} else {
			return statsSeason.getToConcedeAwayRatingX();
		}
	}

	public int getToScoreAwayRating() {
		if (USE_FORM_STATS) {
			return statsForm.getToScoreAwayRating();
		} else {
			return statsSeason.getToScoreAwayRating();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	public int getToConcedeHomeRating() {
		if (USE_FORM_STATS) {
			return statsForm.getToConcedeHomeRating();
		} else {
			return statsSeason.getToConcedeHomeRating();
		}
	}

	public String getHomeStats() {
		if (USE_FORM_STATS) {
			return statsForm.getHomeStats();
		} else {
			return statsSeason.getHomeStats();
		}
	}

	public String getAwayStats() {
		if (USE_FORM_STATS) {
			return statsForm.getAwayStats();
		} else {
			return statsSeason.getAwayStats();
		}
	}


}
