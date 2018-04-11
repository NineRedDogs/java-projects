package com.example.agx;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.AccessToken;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		ConfigStoreOptions fileStore = new ConfigStoreOptions()
				.setType("file")
				.setConfig(new JsonObject().put("path", "src/main/conf/application.json"));
		ConfigRetrieverOptions options = new ConfigRetrieverOptions()
				.addStore(fileStore);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
		retriever.getConfig(ar -> {
			if (ar.failed()) {
				System.err.println("failed to retrieve config.");
			} else {
				config().mergeIn(ar.result());
				startServer();
			}
		});
	}


	void startServer() {
		Router router = Router.router(vertx);

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		//create and register the auth handler to intercept all
		//requests below the /private/ URI:
		AuthHandler authHandler = getOAuthHandler(router);
		router.route("/private/*").handler(authHandler);

		router.route("/private/secret")
		.handler(ctx -> {
			Map claims = getIdClaims(ctx);
			ctx.response().end("Hi "); /** +
                    claims.get("name") +
                    ", the email address we have on file for you is: "+
                    claims.get("email")); */
		});

		vertx.createHttpServer()
		.requestHandler(router::accept)
		.listen(config().getInteger("port"));

	}

	AuthHandler getOAuthHandler(Router router) {
		OAuth2Auth oauth2 = GoogleAuth.create(vertx, config().getString("clientId"), config().getString("clientSecret"));
		OAuth2AuthHandler authHandler = OAuth2AuthHandler.create(oauth2, config().getString("callbackUrl"));
		authHandler.extraParams(new JsonObject("{\"scope\":\"openid profile email\"}"));
		authHandler.setupCallback(router.route());
		return authHandler;
	}

	/**String getEMailAddress() {
		InputStream in = Application.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			    GoogleNetHttpTransport.newTrustedTransport(),
			    JacksonFactory.getDefaultInstance(),
			    clientSecrets,
			    Arrays.asList(
			        PeopleScopes.USERINFO_PROFILE,
			        PeopleScopes.USERINFO_EMAIL
			    )
			)
		    .setAccessType("offline")
		    .build();


		GoogleTokenResponse response = flow
		    .newTokenRequest(code)
		    .setRedirectUri("http://example.com/oauth2callback")
		    .execute();

		Credential credential = flow.createAndStoreCredential(response, null);
		Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(),
		    JacksonFactory.getDefaultInstance(),
		    credential
		)
		    .setApplicationName("My App")
		    .build();

		Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
		    .setApplicationName("My App")
		    .build();
		Userinfoplus userinfo = oauth2.userinfo().get().execute();
		System.out.print(userinfo.toPrettyString());
		
	}*/
	
	Map<String, Object> getIdClaims(RoutingContext ctx) {
	    try {
	    	System.out.println("####### user details : " + ctx.user().principal());
	    	
	    	final String accessToken = ctx.user().principal().getString("access_token");
	    	final String idToken = ctx.user().principal().getString("id_token");
	    	final String expiresAt = ctx.user().principal().getString("expires_at");
	    	
	    	Date expDate = new GregorianCalendar(2020, Calendar.DECEMBER, 31).getTime();
	    	AccessToken aToken = new AccessToken(accessToken, expDate);
	    	/**GoogleCredentials credential = GoogleCredentials.create(aToken);
	    	credential.
	    	GoogleCredentials oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
	    	          "Oauth2").build();
	    	 Userinfoplu userinfo = oauth2.userinfo().get().execute();
	    	 userinfo.toPrettyString(); */
	    	
	    	
	        /*JwtVerifier jwtVerifier = new JwtHelper()
	            .setIssuerUrl(config().getString("issuer"))
	            .setAudience("api://default")
	            .setClientId(config().getString("clientId"))
	            .build();

	        Jwt idTokenJwt = jwtVerifier.decodeIdToken(ctx.user().principal().getString("id_token"), null);
	        return idTokenJwt.getClaims();*/
	    	return null;
	    } catch (Exception e) {
	        //do something with the exception...
	        return new HashMap<>();
	    }
	}

}
