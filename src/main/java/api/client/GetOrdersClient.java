package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrdersClient {
    @Step("Get all orders")
    public Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders/all/");
    }

    @Step("Get the User's Orders")
    public Response getOrdersWithAuthorization(String authorisationToken) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                //.body(loginUserChangeEmailBody)
                .when()
                .get("/api/orders/");
    }
}
