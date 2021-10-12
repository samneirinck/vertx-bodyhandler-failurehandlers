package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.route()
            .failureHandler(ctx -> {
              System.out.println("First error handler");
              ctx.response()
                      .setStatusCode(400)
                      .end("returned in first error handler");
            })
            .failureHandler(ctx -> {
              System.out.println("Second error handler, don't expect to get here");

              ctx.response()
                      .setStatusCode(500)
                      .end("returned in second error handler");
            });

    router.route(HttpMethod.POST, "/fail")
            .handler(BodyHandler.create())
            .handler(ctx -> ctx.response().setStatusCode(200).end(ctx.getBodyAsString()));

    vertx.createHttpServer()
            .requestHandler(router).listen(8888, http -> {
              if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
              } else {
                startPromise.fail(http.cause());
              }
            });
  }
}
