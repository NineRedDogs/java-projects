package com.agrahame.frabbit;

import java.util.ArrayList;

import com.google.api.services.people.v1.model.Person;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.MVELTemplateEngine;

public class Frabbit extends AbstractVerticle {

	public static final String COLLECTION = "frabbitters";
	private MongoClient mongo;
	private Frabbitter topFrab = new Frabbitter();


	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(Frabbit.class);
	}

	@Override
	public void start() throws Exception {

		// config
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

		// set up DB client
		mongo = MongoClient.createShared(vertx, config());
		

	}

	@Override
	public void stop() throws Exception {
		mongo.close();
	}



	void startServer() {
		// In order to use a template we first need to create an engine
	    final HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

		Router router = Router.router(vertx);

		//router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/").handler(StaticHandler.create());

		//create and register the auth handler to intercept all
		//requests below the /private/ URI:
		AuthHandler authHandler = getOAuthHandler(router);
		router.route("/private/*").handler(authHandler);

		MultiMap context = MultiMap.caseInsensitiveMultiMap();
		context.add("ajg1", "cardiff");
		context.add("ajg2", "fulham");

		router.route("/private/landing")
		   .handler(this::returnUserDetails)
		   .handler(TemplateHandler.create(MVELTemplateEngine.create()));
		
		
		/**   .handler(ctx -> {
			   MultiMap hdrs = ctx.request().headers();
			   System.out.println("hdrs : ");
			   ctx.request().headers().set("ajg11", "CardiffCity");
		   })
		   .handler(TemplateHandler.create(MVELTemplateEngine.create())); */
		
		/**   .handler( ctx -> {
			   MultiMap hdrs = ctx.request().headers();
			   System.out.println("hdrs : ");
			   ctx.request().headers().set("ajg11", "CardiffCity");
		   }); */

		/**router.route("/private/2landing")
		.handler(ctx -> {

			System.out.println("CTX: " + ctx.getBodyAsString());
			Map claims = getIdClaims(ctx);
			ctx.response().end("Hi "); +
                    claims.get("name") +
                    ", the email address we have on file for you is: "+
                    claims.get("email")); 
		});*/

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

	/**Map<String, Object> getIdClaims(RoutingContext ctx) {
		try {
			System.out.println("####### user details : " + ctx.user().principal());

			final String accessToken = ctx.user().principal().getString("access_token");
			getFrabitterFromGoogle(accessToken);
			final String idToken = ctx.user().principal().getString("id_token");
			getFrabitterFromGoogle(idToken);
			final String expiresAt = ctx.user().principal().getString("expires_at");

			Date expDate = new GregorianCalendar(2020, Calendar.DECEMBER, 31).getTime();
			AccessToken aToken = new AccessToken(accessToken, expDate);
			GoogleCredentials credential = GoogleCredentials.create(aToken);
	    	credential.
	    	GoogleCredentials oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
	    	          "Oauth2").build();
	    	 Userinfoplu userinfo = oauth2.userinfo().get().execute();
	    	 userinfo.toPrettyString();


			/*JwtVerifier jwtVerifier = new JwtHelper()
	            .setIssuerUrl(config().getString("issuer"))
	            .setAudience("api://default")
	            .setClientId(config().getString("clientId"))
	            .build();

	        Jwt idTokenJwt = jwtVerifier.decodeIdToken(ctx.user().principal().getString("id_token"), null);
	        return idTokenJwt.getClaims();
			return null;
		} catch (Exception e) {
			//do something with the exception...
			return new HashMap<>();
		}
	}*/

	private Future<Void> getFrabitterFromGoogle(final RoutingContext routingContext, final String accessToken) {
		Future<Void> getUserInfoFuture = Future.future();

		WebClient client = WebClient.create(vertx);
		final String header = "Bearer " + accessToken;
		
		// use access token to enquire about authenticated user via Google API rest
		client.getAbs("https://www.googleapis.com/plus/v1/people/me")
		.putHeader("Authorization", header)
		.send(ar -> {
			if (ar.succeeded()) {
				// Obtain REST response
				HttpResponse<Buffer> response = ar.result();

				topFrab = getUserFromGapiJson(response.bodyAsString());
				
				
				// is this user already known to the application ?
				JsonObject query = new JsonObject().put("email", topFrab.getEmailAddress());
				
				final Frabbitter tmp = new Frabbitter();
				// AJG: Follow guide on this page for find & save ...
				
				mongo.find(COLLECTION, query, res -> {
					if (res.succeeded()) {
						boolean foundFrab = false;
						for (JsonObject json : res.result()) {
							System.out.println("Found email : " + json.encodePrettily());
							Frabbitter found = new Frabbitter(json);
							tmp.setId(found.getId());
							System.out.println("id of found : " + found.getId());
							topFrab.setId(found.getId());
							topFrab.setTeam(found.getTeam());
							foundFrab = true;
							break;
						}
						if (!foundFrab) {
							// Not found, so save user 
							mongo.save(COLLECTION, topFrab.toJson(), res2 -> {
								if (res2.succeeded()) {
									if (res2.result() != null) {
										tmp.setId(res2.result());
									}
									System.out.println("Setting ID [" + tmp.getId() + "]");
									topFrab.setId(tmp.getId());
									System.out.println("Saved frabbitter : " + topFrab);
								} else {
									System.out.println("Failed to save frabbitter : " + topFrab);
								}
							});
						}
						
						 /**System.out.println("About to return 200 OK with name [" + topFrab.getFullName() + "]");
						routingContext.response()
						.setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(topFrab.toJson().encodePrettily()); */

						getUserInfoFuture.complete();
					} else {
						System.out.println("Error running query");
					}
				});
				
			} else {
				System.out.println("Something went wrong " + ar.cause().getMessage());
			}
		});
		return getUserInfoFuture;
	}


	private Frabbitter getUserFromGapiJson(final String responseJson) {
		//System.out.println("Received response with status code" + response.statusCode());
		//System.out.println("Received response " + response.bodyAsString());
		//System.out.println("Received response " + response.headers());

		//Gson gson = new GsonBuilder().disableInnerClassSerialization().create();
		Gson gson = new Gson();
		Person p = gson.fromJson(responseJson, Person.class);

		String givenName = "james";
		String familyName = "bond";
		String emailAddr = "james.bond@gov.uk";

		try {
			//System.out.println("people : " + p.toPrettyString());
			//System.out.println("kind : " + p.get("kind"));
			//System.out.println("name : " + p.getNames());
			//System.out.println("name : " + p.get("name"));

			// tried p.getName() but returned null - so had to get name parts manually
			Object nameObj = p.get("name");
			if (nameObj instanceof LinkedTreeMap) { 
				LinkedTreeMap nameElem = (LinkedTreeMap) nameObj;
				familyName = (String) nameElem.get("familyName");
				givenName = (String) nameElem.get("givenName");
			}

			// tried p.getName() but returned null - so had to get name parts manually
			Object emailListObj = p.get("emails");
			//System.out.println("email elem type : " + emailListObj.getClass().getName());

			if (emailListObj instanceof ArrayList) {
				ArrayList emailList = (ArrayList) emailListObj;
				if (emailList != null && !emailList.isEmpty()) {
					for (Object emailObj : emailList) {
						//System.out.println("internal email elem type : " + emailObj.getClass().getName());
						LinkedTreeMap emailElem = (LinkedTreeMap) emailObj;
						//System.out.println("internal email elem : " + emailElem);
						emailAddr = (String) emailElem.get("value");
						// only want the first emaill address
						break;
					}
				}
			}
			System.out.println(" --------------------------------------------------------");
			System.out.println(" --------------------------------------------------------");
			System.out.println("         " + givenName + " " + familyName);
			System.out.println("  email: " + emailAddr);
			System.out.println(" --------------------------------------------------------");
			System.out.println(" --------------------------------------------------------");

		} catch (Exception ex) {
			System.out.println("Caught " + ex.getClass().getName() + " : " + ex.getMessage());
		}
		return new Frabbitter(emailAddr, givenName, familyName);
	}

	private void returnUserDetails(RoutingContext routingContext) {
		System.out.println("About to fetch user data based on google log in .... ");
		JsonObject principal = routingContext.user().principal();
		Future<Void> getUserFuture = getFrabitterFromGoogle(routingContext, principal.getString("access_token"));


		System.out.println("GetUserFuture complete : " + getUserFuture.isComplete());
		// send back a response of current user here ....
		// create a dynamic page with the user info and use templates (e.g. handlebars)

		//System.out.println("About to return 200 OK with name [" + topFrab.getFullName() + "]");
		//routingContext.response()
		//.setStatusCode(200)
		//.putHeader("content-type", "application/json; charset=utf-8")
		//.end(topFrab.toJson().encodePrettily());
		//.end(Json.encodePrettily(user));


		// when get email address from google - can then use a db to maintain list of known users - if email not
		// present in mongo db - pop up a page where user provides details (team, nickname, etc)
		//
	}

	private Future<Void> frabbitterExistsInDb(Frabbitter frab) {
		Future<Void> findFuture = Future.future();
		mongo.findOne(COLLECTION, new JsonObject().put("email", frab.getEmailAddress()), null, ar -> {
			if (ar.succeeded()) {
				if (ar.result() == null) {
					System.out.println("ERROR (1)  Email [" + frab.getEmailAddress() + "] not in db ....");
					findFuture.fail("not in db-1");
				} else {
					Frabbitter dbFrab = new Frabbitter(ar.result());
					System.out.println("Email [" + frab.getEmailAddress() + "] WAS in db ...." + dbFrab.getFullName());
					findFuture.complete();
				}
			} else {
				System.out.println("ERROR (2)  Email [" + frab.getEmailAddress() + "] not in db ....");
				findFuture.fail("not in db-2");
			}
		});
		return findFuture;
	}

	private Future<Void> addFrabbitterToDb(Frabbitter frab) {
		Future<Void> addFuture = Future.future();

		mongo.insert(COLLECTION, frab.toJson(), ar -> {
			if (ar.succeeded()) {
				if (ar.result() == null) {
					System.out.println("ERROR (1)  Email [" + frab.getEmailAddress() + "] not added to db ....");
					addFuture.fail("not added to db-1");
				} else {
					System.out.println("SUCCESS: add Email " + frab.getEmailAddress() + "] to db :-> result [" + ar.result() + "]");
					frab.setId(ar.result());
					addFuture.complete();
				}
			} else {
				System.out.println("ERROR (2)  Email [" + frab.getEmailAddress() + "] not added to db ....");
				addFuture.fail("not added to db-1");
			}
		});
		return addFuture;
	}





}
