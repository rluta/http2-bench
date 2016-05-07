package http2.bench.vertx;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import http2.bench.ServerBase;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Parameters()
public class VertxServer extends ServerBase {

  @Parameter(names = "--internal-blocking-pool-size", description = "Internal blocking pool size", arity = 1)
  public int internalBlockingPoolSize = VertxOptions.DEFAULT_INTERNAL_BLOCKING_POOL_SIZE;

  @Parameter(names = "--open-ssl")
  public boolean openSSL;

  @Parameter(names = "--instances")
  public int instances = 2 * Runtime.getRuntime().availableProcessors();

  public void run() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setInternalBlockingPoolSize(internalBlockingPoolSize));
    DeploymentOptions options = new DeploymentOptions().setInstances(instances);
    options.setConfig(new JsonObject().put("port", httpsPort));
    vertx.deployVerticle(openSSL ? ServerVerticle.OPENSSL.class.getName() : ServerVerticle.JDK.class.getName(), options, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server started");
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}