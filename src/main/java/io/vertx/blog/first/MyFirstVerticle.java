package io.vertx.blog.first;

import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * This is a verticle. A verticle is a _Vert.x component_. This verticle is implemented in Java, but you can
 * implement them in JavaScript, Groovy or even Ruby.
 */
public class MyFirstVerticle extends AbstractVerticle {

	
	// Store our product
	private Map<Integer, Whisky> products = new LinkedHashMap<>();
	// Create some product
	private void createSomeData() {
	  Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
	  products.put(bowmore.getId(), bowmore);
	  Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
	  products.put(talisker.getId(), talisker);
	}
	
  /**
   * This method is called when the verticle is deployed. It creates a HTTP server and registers a simple request
   * handler.
   * <p/>
   * Notice the `listen` method. It passes a lambda checking the port binding result. When the HTTP server has been
   * bound on the port, it call the `complete` method to inform that the starting has completed. Else it reports the
   * error.
   *
   * @param fut the future
   */
  @Override
  public void start(Future<Void> fut) {
	  
	  createSomeData();
	  
	  Router router = Router.router(vertx);
	  
	  router.route("/").handler(routingContext -> {
		 HttpServerResponse response = routingContext.response();
		 response
	       .putHeader("content-type", "text/html")
	       .end("<h1>Hello from my first Vert.x 3 application</h1>");
	 });
	  

	  // Serve static resources from the /assets directory
	  router.route("/assets/*").handler(StaticHandler.create("assets"));

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

}
