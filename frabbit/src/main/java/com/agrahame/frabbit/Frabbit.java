package com.agrahame.frabbit;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.AccessToken;

import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Frabbit extends AbstractVerticle {
	
	
	  // Convenience method so you can run it in your IDE
	  public static void main(String[] args) {
	    Runner.runExample(Frabbit.class);
	  }
	
	@Override
	public void start() throws Exception {
		ConfigStoreOptions fileStore = new ConfigStoreOptions()
				.setType("file")
				.setConfig(new JsonObject().put("path", "config/application.json"));
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

		//router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/").handler(StaticHandler.create());

		//create and register the auth handler to intercept all
		//requests below the /private/ URI:
		AuthHandler authHandler = getOAuthHandler(router);
		router.route("/private/*").handler(authHandler);

		
	    router.route("/private/landing").handler(this::returnUserDetails);
		
		router.route("/private/2landing")
		.handler(ctx -> {
			
			System.out.println("CTX: " + ctx.getBodyAsString());
			Map claims = getIdClaims(ctx);
			ctx.response().end("Hi "); /** +
                    claims.get("name") +
                    ", the email address we have on file for you is: "+
                    claims.get("email")); */
		});

		vertx.createHttpServer()
		.requestHandler(router::accept)
		.listen(config().getInteger("port"));

	    // In order to use a template we first need to create an engine
	    //final HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

	    // Entry point to the application, this will render a custom template.
	    /**router.get().handler(ctx -> {
	      // we define a hardcoded title for our application
	      ctx.put("title", "Seasons of the year");
	      // we define a hardcoded array of json objects
	      JsonArray seasons = new JsonArray();
	      seasons.add(new JsonObject().put("name", "Spring"));
	      seasons.add(new JsonObject().put("name", "Summer"));
	      seasons.add(new JsonObject().put("name", "Autumn"));
	      seasons.add(new JsonObject().put("name", "Winter"));

	      ctx.put("seasons", seasons);

	      // and now delegate to the engine to render it.
	      engine.render(ctx, "templates/index.hbs", res -> {
	        if (res.succeeded()) {
	          ctx.response().end(res.result());
	        } else {
	          ctx.fail(res.cause());
	        }
	      });
	    });

	   // start a HTTP web server on port 8181
	    vertx.createHttpServer().requestHandler(router::accept).listen(8181);*/
	}
	
	/**
	 * When it gets to extract user info - need something along the lines of : 
	 *
	 * 
	    static  String email(JsonObject principal) {
           return idToken(principal).getString("email");
        }

        static  String preferredUsername(JsonObject principal) {
           return idToken(principal).getString("preferred_username");
        }

        static  String nickName(JsonObject principal) {
           return idToken(principal).getString("nickname");
        }
        
	 */
	
	
	AuthHandler getOAuthHandler(Router router) {
		OAuth2Auth oauth2 = GoogleAuth.create(vertx, config().getString("clientId"), config().getString("clientSecret"));
		OAuth2AuthHandler authHandler = OAuth2AuthHandler.create(oauth2, config().getString("callbackUrl"));
		authHandler.extraParams(new JsonObject("{\"scope\":\"openid profile email\"}"));
		authHandler.setupCallback(router.route());
		return authHandler;
	}

	Map<String, Object> getIdClaims(RoutingContext ctx) {
	    try {
	    	System.out.println("####### user details : " + ctx.user().principal());
	    	
	    	final String accessToken = ctx.user().principal().getString("access_token");
	    	getEmail(accessToken);
	    	final String idToken = ctx.user().principal().getString("id_token");
	    	getEmail(idToken);
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
	
	private void getEmail(final String accessToken) {

		String header = "Bearer " + accessToken;
		
    	System.out.println("####### ooooooooooooooooooo #######################################");
    	System.out.println("####### ooooooooooooooooooo #######################################");
		WebClient client = WebClient.create(vertx);

	          client.getAbs("https://www.googleapis.com/plus/v1/people/me")
	            .putHeader("Authorization", header)
	            .send(ar -> {
	              if (ar.succeeded()) {
	            	// Obtain response
	                  HttpResponse<Buffer> response = ar.result();

	                  System.out.println("Received response with status code" + response.statusCode());
	                  System.out.println("Received response " + response.bodyAsString());
	                } else {
	                  System.out.println("Something went wrong " + ar.cause().getMessage());
	                }
	            });

		      	System.out.println("####### ooooooooooooooooooo #######################################");
		      	System.out.println("####### ooooooooooooooooooo #######################################");
	}
	
	private void returnUserDetails(RoutingContext routingContext) {
		System.out.println("About to fetch user data based on google log in .... ");
		//final String id = routingContext.request().getParam("id");
		Map<String, Object> data = routingContext.data();
		if (data == null) {
			routingContext.response().setStatusCode(400).end();
		} else {

			JsonObject principal = routingContext.user().principal();
			
	    	final String accessToken = principal.getString("access_token");
	    	getEmail(accessToken);

			
			System.out.println("principal : " + principal.encodePrettily());
			
			// create a dummy user to test the outbound response
			Frabbitter user = new Frabbitter("Phil Dwyer", "Beast", "pdwyer@cardiffcity.co.uk");

			System.out.println("Data Map size : " + data.size());

			routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(principal.encodePrettily());
			//.end(Json.encodePrettily(user));


			// when get email address from google - can then use a db to maintain list of known users - if email not
			// present in mongo db - pop up a page where user provides details (team, nickname, etc)
			//
			/*mongo.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
		        if (ar.succeeded()) {
		          if (ar.result() == null) {
		            routingContext.response().setStatusCode(404).end();
		            return;
		          }
		          Team team = new Team(ar.result());
		          routingContext.response()
		              .setStatusCode(200)
		              .putHeader("content-type", "application/json; charset=utf-8")
		              .end(Json.encodePrettily(team));
		        } else {
		          routingContext.response().setStatusCode(404).end();
		        }
		      });*/
		}
	}
	


}
