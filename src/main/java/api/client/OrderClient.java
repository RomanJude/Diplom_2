package api.client;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step
    @DisplayName("Check the order's creation without th User's Authorisation")
    public Response creationOrderWithoutAuthorisation(Order order) {
        {
            return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post("/api/orders/");
        }
    }
}
