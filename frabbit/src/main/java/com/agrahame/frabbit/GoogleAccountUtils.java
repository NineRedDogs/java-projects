package com.agrahame.frabbit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleService.People;
import com.google.api.services.people.v1.model.Person;
import com.google.gson.Gson;
//import com.google.api.services.people.v1.PeopleService;

public class GoogleAccountUtils {

	public void setUp() throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// Go to the Google API Console, open your application's
		// credentials page, and copy the client ID and client secret.
		// Then paste them into the following code.
		String clientId = "YOUR_CLIENT_ID";
		String clientSecret = "YOUR_CLIENT_SECRET";

		// Or your redirect URL for web based applications.
		String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
		String scope = "https://www.googleapis.com/auth/contacts.readonly";

		// Step 1: Authorize -->
		String authorizationUrl =
				new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(scope)).build();

		// Point or redirect your user to the authorizationUrl.
		System.out.println("Go to the following link in your browser:");
		System.out.println(authorizationUrl);

		// Read the authorization code from the standard input stream.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What is the authorization code?");
		String code = in.readLine();
		// End of Step 1 <--

		// Step 2: Exchange -->
		GoogleTokenResponse tokenResponse =
				new GoogleAuthorizationCodeTokenRequest(
						httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
				.execute();
		// End of Step 2 <--

		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setClientSecrets(clientId, clientSecret)
				.build()
				.setFromTokenResponse(tokenResponse);

		//peopleService peopleService =
		//    new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
		//...
	}
	//...
	
	private static final String userData="{\n" + 
			" \"kind\": \"plus#person\",\n" + 
			" \"etag\": \"\\\"EhMivDE25UysA1ltNG8tqFM2v-A/F0lJJQDqXo1Gt-lu--_vG1M_5Kg\\\"\",\n" + 
			" \"gender\": \"male\",\n" + 
			" \"emails\": [\n" + 
			"  {\n" + 
			"   \"value\": \"andrew@agrahame.com\",\n" + 
			"   \"type\": \"account\"\n" + 
			"  }\n" + 
			" ],\n" + 
			" \"urls\": [\n" + 
			"  {\n" + 
			"   \"value\": \"http://twitter.com/ninereddogs\",\n" + 
			"   \"type\": \"otherProfile\",\n" + 
			"   \"label\": \"ninereddogs\"\n" + 
			"  },\n" + 
			"  {\n" + 
			"   \"value\": \"mailto:andrew@agrahame.com\",\n" + 
			"   \"type\": \"otherProfile\",\n" + 
			"   \"label\": \"andrew@agrahame.com\"\n" + 
			"  }\n" + 
			" ],\n" + 
			" \"objectType\": \"person\",\n" + 
			" \"id\": \"117966152726172039354\",\n" + 
			" \"displayName\": \"Andrew Grahame\",\n" + 
			" \"name\": {\n" + 
			"  \"familyName\": \"Grahame\",\n" + 
			"  \"givenName\": \"Andrew\"\n" + 
			" },\n" + 
			" \"url\": \"https://plus.google.com/117966152726172039354\",\n" + 
			" \"image\": {\n" + 
			"  \"url\": \"https://lh5.googleusercontent.com/-HEZVMOHqmgM/AAAAAAAAAAI/AAAAAAAAM1s/bcHRtVAg0YI/photo.jpg?sz=50\",\n" + 
			"  \"isDefault\": false\n" + 
			" },\n" + 
			" \"isPlusUser\": true,\n" + 
			" \"language\": \"en_GB\",\n" + 
			" \"circledByCount\": 18,\n" + 
			" \"verified\": false,\n" + 
			" \"domain\": \"agrahame.com\"\n" + 
			"}";

	private static void parseUserJson(final String userJson) {
		Gson gson = new Gson();
		Person p = gson.fromJson(userJson, Person.class);
		
		try {
			System.out.println("people : " + p.toString());
			System.out.println("kind : " + p.get("kind"));
			System.out.println("emails : " + p.get("emails"));
			System.out.println("name : " + p.get("name"));
			String firstName = p.get("givenName").toString();
			String lastName = p.get("familyName").toString();
			System.out.println("Mr " + firstName + " " + lastName);
		} catch (Exception ex) {
			System.out.println("Caught " + ex.getClass().getName() + " : " + ex.getMessage());
		}

	}

	public static void main(String[] args) {
		parseUserJson(userData);

	}
}
