package example;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;

public class ProductBulkCreateSimulation extends Simulation {

  private static final HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8082")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  private static final ScenarioBuilder scenario = scenario("Bulk Product Creation")
      .exec(
          http("Bulk Create Products")
              .post("/products")
              .check(status().is(201))
      );

  private static final Assertion assertion = global().failedRequests().count().lt(1L);

  {
    int totalRequests = 900000;
    int maxConcurrentUsers = 1500;

    double individualUserRequests = (double) totalRequests / maxConcurrentUsers;
    Duration simulationDuration = Duration.ofSeconds((long) individualUserRequests);

    setUp(
        scenario.injectOpen(
            constantUsersPerSec(maxConcurrentUsers).during(simulationDuration)
        )
    )
        .assertions(assertion)
        .protocols(httpProtocol);
  }
}
