package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ChangeUserClient {
    @Step("Change User's data with the User's Authorization")
    public Response changeUserDataWithAuthorization(String authorisationToken, String loginUserChangeBody) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                .body(loginUserChangeBody)
                .when()
                .patch("/api/auth/user/");
    }

    @Step("Change User's Email with the User's Authorization")
    public Response changeUserEmailWithAuthorization(String authorisationToken, String loginUserChangeEmailBody) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                .body(loginUserChangeEmailBody)
                .when()
                .patch("/api/auth/user/");
    }

    @Step("Change User's Password with the User's Authorization")
    public Response changeUserPasswordWithAuthorization(String authorisationToken, String loginUserChangePasswordBody) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                .body(loginUserChangePasswordBody)
                .when()
                .patch("/api/auth/user/");
    }

    @Step("Change User's Name with the User's Authorization")
    public Response changeUserNameWithAuthorization(String authorisationToken, String loginUserChangeNameBody) {
        return given()
                .header("Authorization", authorisationToken)
                .and()
                .body(loginUserChangeNameBody)
                .when()
                .patch("/api/auth/user/");
    }

    @Step("Change User's data without the User's Authorization")
    public Response changeUserDataWithoutAuthorization(String loginUserChangeBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUserChangeBody)
                .when()
                .patch("/api/auth/user/");
    }
}
