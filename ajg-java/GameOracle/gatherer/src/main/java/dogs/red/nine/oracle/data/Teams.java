package dogs.red.nine.oracle.data;

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
		final String htName = match.getHomeTeam().getName();
		if (!teamMap.containsKey(htName)) {
			teamMap.put(htName, new Team(htName));
		}
		teamMap.get(htName).addResult(match);

		final String atName = match.getAwayTeam().getName();
		if (!teamMap.containsKey(atName)) {
			teamMap.put(atName, new Team(atName));
		}
		teamMap.get(atName).addResult(match);
	}


	public void updateStats() {
		for(Map.Entry<String, Team> team : teamMap.entrySet()){
            System.out.println("Updating stats for team : " + team.getKey());
            team.getValue().updateStats();
        }
	}

	public Team getTeam(String name) {
		return teamMap.get(name);
	}
	
	public String displayTeamStats() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Team> team : teamMap.entrySet()){
            sb.append("Showing stats for team : " + team.getKey());
            sb.append(team.getValue().toString());			
		}
		return sb.toString();
	}
	

}
