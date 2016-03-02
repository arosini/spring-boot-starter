package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class RequestLimitFilterTests extends AbstractIntegrationTests {

  @Test
  public void requestLimit_overTheLimit() {
    for (int x = 0; x <= 100; x++) {
      lastResponse = given().accept(ContentType.JSON).get(baseUrl);
    }

    lastResponse.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
        .body("errors[0].message",
            equalTo("The request limit for this week has been reached (sorry, I don't want to be charged)."));
  }

  @Test
  public void requestLimit_underTheLimit() {
    for (int x = 0; x < 100; x++) {
      lastResponse = given().accept(ContentType.JSON).get(baseUrl);
    }

    assertOkResponse();
  }

}
