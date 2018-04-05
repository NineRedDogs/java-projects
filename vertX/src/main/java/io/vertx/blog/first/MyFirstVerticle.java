package io.vertx.blog.first;

import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Taken from : https://vertx.io/blog/combine-vert-x-and-mongo-to-build-a-giant/
 * 
 * @author agrahame
 *
 */
public class MyFirstVerticle extends AbstractVerticle {

	private Map<Integer, Team> products = new LinkedHashMap<>();

	private void createSomeData() {
		Team cardiff = new Team("Cardiff City", "Wales");
		products.put(cardiff.getId(), cardiff);
		Team liverpool = new Team("Liverpool", "England");
		products.put(liverpool.getId(), liverpool);
	}

	@Override
	public void start(Future<Void> fut) {

		// set up some data
		createSomeData();

		// Create a router object.
		Router router = Router.router(vertx);

		// Bind "/" to our hello message - so we are still compatible.
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response
			.putHeader("content-type", "text/html")
			.end("<h1>Hello from my first Vert.x 3 application</h1>");
		});

		// Static handler
		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.get("/api/teams").handler(this::getAll);
		router.route("/api/teams*").handler(BodyHandler.create());
		router.post("/api/teams").handler(this::addOne);
		router.delete("/api/teams/:id").handler(this::deleteOne);
		router.get("/api/teams/:id").handler(this::getOne);
		router.put("/api/teams/:id").handler(this::updateOne);

		// Create the HTTP server and pass the "accept" method to the request handler.
		vertx
		.createHttpServer()
		.requestHandler(router::accept)
		.listen(
				// Retrieve the port from the configuration,
				// default to 8080.
				config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				}
				);
	}

	private void getAll(RoutingContext routingContext) {
		routingContext.response()
		.putHeader("content-type",  "application/json; charset=utf-8")
		.end(Json.encodePrettily(products.values()));
	}

	private void addOne(RoutingContext routingContext) {
		final Team newTeam = Json.decodeValue(routingContext.getBodyAsString(),  Team.class);
		products.put(newTeam.getId(), newTeam);
		routingContext.response()
		.setStatusCode(201)
		.putHeader("content-type",  "application/json; charset=utf-8")
		.end(Json.encodePrettily(newTeam));
	}

	private void getOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			final Integer idAsInteger = Integer.valueOf(id);
			Team team = products.get(idAsInteger);
			if (team == null) {
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(team));
			}
		}
	}

	private void updateOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		JsonObject json = routingContext.getBodyAsJson();
		if (id == null || json == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			final Integer idAsInteger = Integer.valueOf(id);
			Team updatedTeam = products.get(idAsInteger);
			if (updatedTeam == null) {
				routingContext.response().setStatusCode(404).end();
			} else {
				updatedTeam.setName(json.getString("name"));
				updatedTeam.setCountry(json.getString("country"));
				routingContext.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(updatedTeam));
			}
		}
	}

	private void deleteOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Integer idAsInteger = Integer.valueOf(id);
			products.remove(idAsInteger);
			routingContext.response().setStatusCode(204).end();
		}
	}

}
