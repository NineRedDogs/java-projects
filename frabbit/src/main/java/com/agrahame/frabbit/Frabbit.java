package com.agrahame.frabbit;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.ext.web.handler.TemplateHandler.DEFAULT_CONTENT_TYPE;
import static io.vertx.ext.web.handler.TemplateHandler.DEFAULT_TEMPLATE_DIRECTORY;

import java.util.ArrayList;

import com.google.api.services.people.v1.model.Person;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.impl.Utils;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

public class Frabbit extends AbstractVerticle {

	public static final String COLLECTION = "frabbitters";
	private static final String QUERY_EQUALS = "=";
	private MongoClient mongo;
	private Frabbitters frabs = new Frabbitters();

	//private Frabbitter topFrab = new Frabbitter();

	private HandlebarsTemplateEngine engine;



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

		// read in all known users
		initUsers();

		// create template engine
		engine = HandlebarsTemplateEngine.create();
	}

	private void initUsers() {
		JsonObject query = new JsonObject();
		mongo.find(COLLECTION, query, res -> {
			if (res.succeeded()) {
				frabs.initialise(res.result());
			} else {
				res.cause().printStackTrace();
			}
		});
	}

	@Override
	public void stop() throws Exception {
		mongo.close();
	}


	void startServer() {

		Router router = Router.router(vertx);

		//router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/").handler(StaticHandler.create());

		//create and register the auth handler to intercept all
		//requests below the /private/ URI:
		AuthHandler authHandler = getOAuthHandler(router);
		router.route("/private/*").handler(authHandler); //.handler(this::agHdlr);

		router.route("/private/landing")
		.handler(this::getGoogleUserData);
		/**.handler(this::routeHandler)
		   .handler(this::templateHandler).failureHandler(rc -> {
				System.out.println("Error while rendering template : " + rc.get("tmplName") + " : ");
				Throwable f = rc.failure();
				if (f != null) {
					System.out.println("  failure: " + f.getClass().getName() + " f:" + f.getMessage());
				} else {
					System.out.println("no failure in routing context !!!");
				}
				rc.response().setStatusCode(500).end();
			}); */

		//router.routeWithRegex("/(.*)").handler(this::routeHandler).failureHandler(rc -> {
		//	System.out.println("Error handling request {" + rc.request().absoluteURI() + "}" + rc.failure());
		//	rc.response().setStatusCode(500).end();
		//});

		//
		// based on the following comment from the mesh example - need to see how we get context data across the route handlers and into the templates
		//

		// Finally use the previously set context data to render the templates
		router.route().handler(this::routeHandler).handler(this::templateHandler).failureHandler(rc -> {
			//router.route().handler(this::templateHandler).failureHandler(rc -> {
			System.out.println("Error while rendering template : " + rc.get("tmplName") + " : ");
			Throwable f = rc.failure();
			if (f != null) {
				System.out.println("  failure: " + f.getClass().getName() + " f:" + f.getMessage());
			} else {
				System.out.println("no failure in routing context !!!");
			}
			rc.response().setStatusCode(500).end();
		});

		vertx.createHttpServer()
		.requestHandler(router::accept)
		.listen(config().getInteger("port"));
	}

	AuthHandler getOAuthHandler(Router router) {
		OAuth2Auth oauth2 = GoogleAuth.create(vertx, config().getString("clientId"), config().getString("clientSecret"));
		OAuth2AuthHandler authHandler = OAuth2AuthHandler.create(oauth2, config().getString("callbackUrl"));
		authHandler.extraParams(new JsonObject("{\"scope\":\"openid profile email\"}"));
		//authHandler.setupCallback(router.route()); // ajg added callback param here
		authHandler.setupCallback(router.route("/callback")); // ajg added callback param here
		return authHandler;
	}

	private void getGoogleUserData(RoutingContext rc) {
		System.out.println("getGoogleUserData, path : " + rc.normalisedPath());

		System.out.println("About to fetch user data based on google log in .... ");
		JsonObject principal = rc.user().principal();

		// send off request to google api to get user details from auth access token
		WebClient client = WebClient.create(vertx);
		final String header = "Bearer " + principal.getString("access_token");

		// use access token to enquire about authenticated user via Google API rest
		client.getAbs("https://www.googleapis.com/plus/v1/people/me")
		      .putHeader("Authorization", header)
		      .send(ar -> {
		    	  if (ar.succeeded()) {
		    		  
		    		  // Google API call successful, now use REST response to create new user object
		    		  Frabbitter googleUser = getUserFromGapiJson(ar.result().bodyAsString());
		    		  
		    		  // Get User Id matching the current users email address ??
		    		  final String userId = frabs.getIdFromEmail(googleUser.getEmailAddress());

		    		  // is this a new user to the app ???
		    		  if (userId == null) {

		    			  // Not found, so save new user to DB
		    			  mongo.save(COLLECTION, googleUser.toJson(), res -> {
		    				  if (res.succeeded()) {
		    					  final String newUserId = res.result(); 
		    					  if (newUserId != null) {

		    						  // successfully added user to db
			    					  System.out.println("New user [" + googleUser + "] ... added to rc");
		    						  googleUser.setId(newUserId);
		    						  
		    						  // add new user to local map
		    						  frabs.put(googleUser);
		    						  
		    						  // send 302 here with new userId in query param ....
				    				  System.out.println("About to redirect (302) Getting frabbitId ctx var [" + googleUser + "]");

				    				  rc.response()
				    				  .putHeader("location", ("/page1.html?frabbitId=" + googleUser.getId()))
				    				  .setStatusCode(302).end();
		    					  } else {
			    					  System.out.println("New user not added to DB, sending 500 ....");
			    					  rc.response().setStatusCode(500).end();
		    					  }
		    				  } else {
		    					  System.out.println("Failed to save new frabbitter : " + googleUser + " sending 500");
		    					  rc.response().setStatusCode(500).end();
		    				  }
		    			  });
		    		  } else {
		    			  // user already existed, just need to set its id 
		    			  //googleUser.setId(userId); 
			    		  //rc.put("user", googleUser.getId());
	    				  System.out.println("User already exists, no need to add to DB, just redirect (302) setting frabbitId query [" + userId + "]");

	    				  rc.response()
	    				  .putHeader("location", ("/page1.html?frabbitId=" + userId))
	    				  .setStatusCode(302).end();
		    		  }
		    	  } else {
		    		  System.out.println("Error running query");
					  rc.response().setStatusCode(500).end();
		    	  }
				});
	}

	public void agHdlr(RoutingContext rc) {
		System.out.println("Inside agHdlr handler ....");
		System.out.println("path : " + rc.normalisedPath());
	}

	public void routeHandler(RoutingContext rc) {
		System.out.println("Inside route handler ....");
		System.out.println("Query [" + rc.request().query() + "]");
		String[] queryElems = rc.request().query().split(QUERY_EQUALS);
		
		if (queryElems.length != 2) {
			// expected only one query param, i.e should be 2 elems in array, i.e.
			// frabbitId=5ad05296c6871b3f7c0c2291
			System.out.println("error, unexpected query param syntax [" + rc.request().query() + "] sending 500 .....");
			rc.response().setStatusCode(500).end();
		} else {
			// extract user id from query elems
			final String currUserId = queryElems[1];
			System.out.println("curr user id [" + currUserId + "]");
			final Frabbitter currUser = frabs.get(currUserId);
			
			if (currUser == null) {
				// could not find user matching id in map
				System.out.println("error, user not found in map - send error resposne ????");
				rc.response().setStatusCode(500).end();
			} else {
				// good, got a user id from query param, now get user from map
				System.out.println("Retrieved current user from map [" + currUser + "] now setting template data  up ....");
				
				rc.put("tmplName", "landing");

				rc.put("title", ("Frabbit user : " + currUser.getFullName()));
				// we define a hardcoded array of json objects
				JsonArray users = new JsonArray();
				users.add(currUser.toJson());
				rc.put("users", users);

				System.out.println("Just set template [" + rc.get("tmplName") + "] move on to template handler ....");
				rc.next();
				return;
			}
		}
	}

	private void restOfRouteHandler(RoutingContext rc) {
		System.out.println("Inside route handler, normalised path : " + rc.normalisedPath());

		Frabbitter currUser = rc.get("user");
		System.out.println("About to return 200 OK with name [" + currUser.getFullName() + "]");
		rc.response()
		.setStatusCode(200)
		.putHeader("content-type", "application/json; charset=utf-8")
		.end(currUser.toJson().encodePrettily());



		String path = rc.pathParam("param0");
		System.out.println("Inside route handler : path [" + path + "]");

		if (path != null) {
			if (path.equals("favicon.ico")) {
				rc.response().setStatusCode(404).end("Not found");
				return;
			}

			// Render the welcome page for root page requests
			if (path.isEmpty()) {
				System.out.println("***Empty path !!!!");
				/**loadTopNav().subscribe(sub -> {
				rc.put("data", sub.getData());
				rc.put("tmplName", "welcome");
				rc.next();
			});*/
				return;
			}

			System.out.println("**** Handling request for path {" + path + "}");

			if ((path.endsWith(".jpg")) || (path.endsWith(".jpeg"))) {
				handleImages(path, rc);
			} else {
				handlePage(path, rc);
			}
		} else {
			System.out.println("Path value was null .... cant process any further");
		}
	}

	private void handlePage(String path, RoutingContext rc) {
		System.out.println("**** Handling page: path {" + path + "}");
	}

	private void handleImages(String path, RoutingContext rc) {
		System.out.println("**** Handling image (jpg): path {" + path + "}");
	}




	private Frabbitter getUserFromGapiJson(final String responseJson) {
		Gson gson = new Gson();
		Person p = gson.fromJson(responseJson, Person.class);

		String givenName = "james";
		String familyName = "bond";
		String emailAddr = "james.bond@gov.uk";

		try {
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

	private void templateHandler(RoutingContext rc) {
		System.out.println("*** Inside template handler");
		String file = rc.get("tmplName");
		engine.render(rc, DEFAULT_TEMPLATE_DIRECTORY, Utils.normalizePath(file), res -> {
			if (res.succeeded()) {
				System.out.println("*** template success !!!");
				rc.response().putHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE).end(res.result());
			} else {
				System.out.println("*** template failed !!!");
				rc.fail(res.cause());
			}
		});
	}

}
