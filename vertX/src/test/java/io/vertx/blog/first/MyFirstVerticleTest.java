package io.vertx.blog.first;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MyFirstVerticleTest {

	private ServerSocket socket;
	private int port=0;
	private Vertx vertx;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
	try {
		socket = new ServerSocket(0);
	    port = socket.getLocalPort();
	    socket.close();
	    
	    DeploymentOptions options = new DeploymentOptions()
	    	    .setConfig(new JsonObject().put("http.port", port)
	    	);
	    vertx.deployVerticle(MyFirstVerticle.class.getName(), options,
	        context.asyncAssertSuccess());
	} catch (IOException ioe) {
		System.out.println("Caught IOException when creating new server socket : " + ioe.getMessage());
	}
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }
  
  @Test
  public void checkThatTheIndexPageIsServed(TestContext context) {
    Async async = context.async();
    vertx.createHttpClient().getNow(port, "localhost", "/assets/index.html", response -> {
      context.assertEquals(response.statusCode(), 200);
      context.assertEquals(response.headers().get("content-type"), "text/html");
      response.bodyHandler(body -> {
        context.assertTrue(body.toString().contains("<title>My Team Collection</title>"));
        async.complete();
      });
    });
  }
  
  @Test
  public void testMyApplication(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().getNow(port, "localhost", "/",
     response -> {
      response.handler(body -> {
        context.assertTrue(body.toString().contains("from my"));
        async.complete();
      });
    });
  }
  
  @Test
  public void checkThatWeCanAdd(TestContext context) {
    Async async = context.async();
    final String json = Json.encodePrettily(new Team("Arsenal", "England"));
    final String length = Integer.toString(json.length());
    vertx.createHttpClient().post(port, "localhost", "/api/teams")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(response -> {
          context.assertEquals(response.statusCode(), 201);
          context.assertTrue(response.headers().get("content-type").contains("application/json"));
          response.bodyHandler(body -> {
            final Team newTeam = Json.decodeValue(body.toString(), Team.class);
            context.assertEquals(newTeam.getName(), "Arsenal");
            context.assertEquals(newTeam.getCountry(), "England");
            context.assertNotNull(newTeam.getId());
            async.complete();
          });
        })
        .write(json)
        .end();
  }
  
  
  
}