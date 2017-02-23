package com.ninereddogs.item2;

public class FootballResult {
	
	
	private final String homeTeamName;
	private final String awayTeamName;
	private final int homeTeamScore;
	private final int awayTeamScore;
	private final int homeTeamHTScore;
	private final int awayTeamHTScore;
	
	
	public static class Builder {
		
		//required
		private final String homeTeamName;
		private final String awayTeamName;
		private final int homeTeamScore;
		private final int awayTeamScore;
		
		//optional
		private int homeTeamHTScore = 0;
		private int awayTeamHTScore = 0;
		
		public Builder(String ht, int htScore, int atScore, String at) {
			this.homeTeamName = ht;
			this.homeTeamScore = htScore;
			this.awayTeamScore = atScore;
			this.awayTeamName = at;
		}
		
		public Builder homeTeamHTScore(int htScore) {
			this.homeTeamHTScore = htScore;
			return this;
		}
		
		public Builder awayTeamHTScore(int atScore) {
			this.awayTeamHTScore = atScore;
			return this;
		}
		
		public FootballResult build() {
			return new FootballResult(this);
		}
	}
	
	private FootballResult(Builder builder) {
		homeTeamName = builder.homeTeamName;
		awayTeamName = builder.awayTeamName;
		homeTeamScore = builder.homeTeamScore;
		awayTeamScore = builder.awayTeamScore;
		homeTeamHTScore = builder.homeTeamHTScore;
		awayTeamHTScore = builder.homeTeamHTScore;
	}
	
	
	
	public static void main(String[] args) {
		FootballResult res1 = new FootballResult.Builder("Cardiff", 3, 1, "Swansea").homeTeamHTScore(1).awayTeamHTScore(1).build();

		FootballResult res2 = new FootballResult.Builder("Swansea", 0, 0, "Cardiff").build();

	}
	

}
