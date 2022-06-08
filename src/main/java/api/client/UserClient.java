package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    @Step("Create User")
    public Response creationUser(String body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/auth/register/");
    }

    @Step("Delete User")
    public Response deleteUser(String body, String authorisationToken) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                .body(body)
                .when()
                .delete("/api/auth/user/");
    }
}