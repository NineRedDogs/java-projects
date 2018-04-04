package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Taken from : https://vertx.io/blog/my-first-vert-x-3-application/
 * 
 * @author agrahame
 *
 */
public class MyFirstVerticle extends AbstractVerticle {
	
	  @Override
	  public void start(Future<Void> fut) {
	    vertx
	        .createHttpServer()
	        .requestHandler(r -> {
	          r.response().end("<h1>You've hit my VertX app - cheers !!! </h1>");
	        })
	        .listen(
	        		// Retrieve the listen port from config (def: 8080)
	        		config().getInteger("http.port", 8080),
	        		result -> {
	        			if (result.succeeded()) {
	        				fut.complete();
	        			} else {
	        				fut.fail(result.cause());
	        			}
	        		});
	  }

}
