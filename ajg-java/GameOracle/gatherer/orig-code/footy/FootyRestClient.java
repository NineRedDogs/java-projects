package dogs.red.nine.footy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class FootyRestClient {
	
	// see http://api.football-data.org/index

	//final static String SAMPLE_URI = "http://api.football-data.org/alpha/soccerseasons/";
	//final static String SAMPLE_URI = "http://api.football-data.org/alpha/soccerseasons/356/fixtures";
	final static String SEASON_PREFIX = "http://api.football-data.org/alpha/soccerseasons/";
	
	final static String FIXTURES = "http://api.football-data.org/alpha/soccerseasons/398/fixtures/?matchday=10";
	
	/**
	 * 
	soccerseasons/394/,"1. Bundesliga 2015/16","league":"BL1","year":"2015","numberOfTeams":18,"numberOfGames":306
	soccerseasons/395/,"2. Bundesliga 2015/16","league":"BL2","year":"2015","numberOfTeams":18,"numberOfGames":306
	soccerseasons/396/,"Ligue 1 2015/16","league":"FL1","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/397/,"Ligue 2 2015/16","league":"FL2","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/398/,"Premier League 2015/16","league":"PL","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/399/,"Primera Division 2015/16","league":"PD","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/400/,"Segunda Division 2015/16","league":"SD","year":"2015","numberOfTeams":22,"numberOfGames":462
	soccerseasons/401/,"Serie A 2015/16","league":"SA","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/402/,"Primeira Liga 2015/16","league":"PPL","year":"2015","numberOfTeams":18,"numberOfGames":306
	soccerseasons/403/,"3. Bundesliga 2015/16","league":"BL3","year":"2015","numberOfTeams":20,"numberOfGames":380
	soccerseasons/404/,"Eredivisie 2015/16","league":"DED","year":"2015","numberOfTeams":18,"numberOfGames":306
	soccerseasons/405/,"Champions League 2015/16","league":"CL","year":"2015","numberOfTeams":32,"numberOfGames":96
	
		 */


	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String BETA_DEV_API_TOKEN = "c1b833ada8ef49858bccb4b9495436de";


	private static String sendGET(final String getUri) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(getUri);
		/**
		httpGet.addHeader("User-Agent", USER_AGENT);
		 
		httpGet.addHeader("X-Auth-Token", BETA_DEV_API_TOKEN);
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		System.out.println("GET Response Status:: "
				+ httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent()));
				

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		//System.out.println(response.toString());
		httpClient.close();
		
		return response.toString();**/
		return "";
	}

	
	public static String toPrettyFormat(String jsonString) 
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }
	

	public final static void main(String[] args) throws IOException {

			final String seasonId=FIXTURES;

			final String resp = sendGET(seasonId);

			System.out.println("Response : " + resp);

			final String respPretty = toPrettyFormat(resp);
			System.out.println("Response : " + respPretty);

	}


}
