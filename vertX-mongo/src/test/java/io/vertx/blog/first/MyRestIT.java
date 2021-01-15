package io.vertx.blog.first;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests checks our REST API.
 */
public class MyRestIT {

  @BeforeClass
  public static void configureRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = Integer.getInteger("http.port", 8082);
  }

  @AfterClass
  public static void unconfigureRestAssured() {
    RestAssured.reset();
  }

  @Test
  public void checkThatWeCanRetrieveIndividualProduct() {
    // Get the list of bottles, ensure it's a success and extract the first id.
    final String id = get("/api/teams").then()
        .assertThat()
        .statusCode(200)
        .extract()
        .jsonPath().getString("find { it.name=='Liverpool' }.id");

    // Now get the individual resource and check the content
    get("/api/teams/" + id).then()
        .assertThat()
        .statusCode(200)
        .body("name", equalTo("Liverpool"))
        .body("country", equalTo("England"))
        .body("id", equalTo(id));
  }

  @Test
  public void checkWeCanAddAndDeleteAProduct() {
    // Create a new bottle and retrieve the result (as a teams instance).
    Team team = given()
        .body("{\"name\":\"Chelsea\", \"country\":\"England\"}").request().post("/api/teams").thenReturn().as(Team.class);
    assertThat(team.getName()).isEqualToIgnoringCase("Chelsea");
    assertThat(team.getCountry()).isEqualToIgnoringCase("England");
    assertThat(team.getId()).isNotEmpty();



    // Check that it has created an individual resource, and check the content.
    get("/api/teams/" + team.getId()).then()
        .assertThat()
        .statusCode(200)
        .body("name", equalTo("Chelsea"))
        .body("country", equalTo("England"))
        .body("id", equalTo(team.getId()));



    // Delete the bottle
    delete("/api/teams/" + team.getId()).then().assertThat().statusCode(204);

    // Check that the resource is not available anymore
    get("/api/teams/" + team.getId()).then()
        .assertThat()
        .statusCode(404);

  }
}
