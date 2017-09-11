package dogs.red.nine.footy;

import java.util.HashMap;
import java.util.Map;

public class Teams {
	
	private Map<String, Team> teamMap;

	/**
	 * ctor
	 */
	public Teams() {
		super();
		this.teamMap = new HashMap<String, Team>();
	}
	
	
	public void updateTeams(final String teamName) {
		if (!teamMap.containsKey(teamName)) {
			// map doesn't contain this team yet, so we can add it
			teamMap.put(teamName, new Team(teamName));
		}
	}


	public void addMatchResult(MatchData match) {
		Team ht = teamMap.get(match.getHomeTeam().getName());
		if (ht != null) {
			ht.addResult(match);
		}
		Team at = teamMap.get(match.getAwayTeam().getName());
		if (at != null) {
			at.addResult(match);
		}
	}


	public void updateStats() {
		for(Map.Entry<String, Team> team : teamMap.entrySet()){
            //System.out.println("Updating stats for team : " + team.getKey());
            team.getValue().updateStats();
        }
	}

	public Team getTeam(String name) {
		return teamMap.get(name);
	}
	

}
