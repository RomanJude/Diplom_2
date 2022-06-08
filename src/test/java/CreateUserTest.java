import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {

    private final String email = "vasya302@yandex.ru";
    private final String password = "1237897";
    private final String name = "maxim";
    private final String createUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String createUserWithoutEmailBody = "{\"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String createUserWithoutPasswordBody = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
    private final String createUserWithoutNameBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    private String authorisationToken;

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        TimeUnit.SECONDS.sleep(40);
    }

    @Test
    @DisplayName("Check the User creation with all needed fields")
    public void creationUserTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        createUserResponse.then().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User creation with the same fields")
    public void createUserWithTheSameParametersTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        Response createUserWithTheSameParametersResponse = userClient.creationUser(createUserBody);
        createUserWithTheSameParametersResponse.then().statusCode(403).and().assertThat().body("success", equalTo(false),
                "message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check the User creation without E-mail")
    public void createUserWithoutEmailTest() {
        UserClient userClient = new UserClient();
        Response createUserWithoutEmailResponse = userClient.creationUser(createUserWithoutEmailBody);
        createUserWithoutEmailResponse.then().statusCode(403).and().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check the User creation without Password")
    public void createUserWithoutPasswordTest() {
        UserClient userClient = new UserClient();
        Response createUserWithoutPasswordResponse = userClient.creationUser(createUserWithoutPasswordBody);
        createUserWithoutPasswordResponse.then().statusCode(403).and().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check the User creation without Name")
    public void createUserWithoutNameTest() {
        UserClient userClient = new UserClient();
        Response createUserWithoutNameResponse = userClient.creationUser(createUserWithoutNameBody);
        createUserWithoutNameResponse.then().statusCode(403).and().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void cleanUp() {
        UserClient userClient = new UserClient();
        if (authorisationToken != null) {
            String deleteUserClientBody = "{\"Authorization\": " + authorisationToken + "}";
            userClient.deleteUser(deleteUserClientBody, authorisationToken);
        }
    }
}