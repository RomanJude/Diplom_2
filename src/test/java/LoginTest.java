import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;

import static org.hamcrest.Matchers.*;

import api.client.LoginClient;

import java.util.concurrent.TimeUnit;

public class LoginTest {

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        TimeUnit.SECONDS.sleep(40);
    }

    private final String email = "vasya2700@yandex.ru";
    private final String password = "1237897";
    private final String name = "maxim";
    private final String wrongEmail = "petya@mail.ru";
    private final String wrongPassword = "123789700";
    private final String createUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String loginUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    private final String loginUserWithWrongEmailBody = "{\"wrongEmail\": \"" + wrongEmail + "\", \"password\": \"" + password + "\"}";
    private final String loginUserWithWrongPasswordBody = "{\"email\": \"" + email + "\", \"wrongPassword\": \"" + wrongPassword + "\"}";
    private String authorisationToken;

        @Test
    @DisplayName("Check the User authorisation with all needed fields")
    public void loginWithRightFieldsTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        Response loginUserResponse = login.loginUser(loginUserBody);
        loginUserResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User authorisation with wrong email")
    public void loginUserWithWrongEmailTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        Response loginUserResponse = login.loginUser(loginUserWithWrongEmailBody);
        loginUserResponse.then().statusCode(401).and().assertThat().body("success", equalTo(false),
                "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check the User authorisation with wrong password")
    public void loginUserWithWrongPasswordTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        Response loginUserResponse = login.loginUser(loginUserWithWrongPasswordBody);
        loginUserResponse.then().statusCode(401).and().assertThat().body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @After
    public void cleanUp() {
        UserClient userClient = new UserClient();
        String deleteUserClientBody = "{\"Authorization\": " + authorisationToken + "}";
        userClient.deleteUser(deleteUserClientBody, authorisationToken);
    }
}