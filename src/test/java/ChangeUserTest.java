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
import api.client.ChangeUserClient;

import java.util.concurrent.TimeUnit;

public class ChangeUserTest {

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        TimeUnit.SECONDS.sleep(40);
    }

    private final String email = "vasya1700@yandex.ru";
    private final String password = "1237897";
    private final String name = "maxim";

    private final String emailChanged = "65555marwell@yandex.ru";
    private final String passwordChanged = "7555mewmewmew";
    private final String nameChanged = "87777rocket";

    private final String emailChangedOnly = "markus102@mail.com";
    private final String passwordChangeOnly = "Red Army";
    private final String nameChangeOnly = "Kutuzoff";


    private final String createUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String loginUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    private final String loginUserChangeBody = "{\"emailChanged\": \"" + emailChanged + "\", " +
            "\"passwordChanged\": \"" + passwordChanged + "\", \"nameChanged\": \"" + nameChanged + "\"}";
    private final String loginUserChangeEmailBody = "{\"emailChangedOnly\": \"" + emailChangedOnly + "\"}";
    private final String loginUserChangePasswordBody = "{\"passwordChanged\": \"" + passwordChanged + "\" }";
    private final String loginUserChangeNameBody = "{\"nameChanged\": \"" + nameChanged + "\" }";
    private String authorisationToken;

    @Test
    @DisplayName("Check the User's changing data after the User's Authorization")
    public void changeUserDataWithAuthorizationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ChangeUserClient changeUserClient = new ChangeUserClient();
        Response changeUserClientResponse = changeUserClient.changeUserDataWithAuthorization(authorisationToken, loginUserChangeBody);
        changeUserClientResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User's changing Email after the User's Authorization")
    public void changeUserEmailWithAuthorizationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ChangeUserClient changeUserClient = new ChangeUserClient();
        Response changeUserClientResponse = changeUserClient.changeUserEmailWithAuthorization(authorisationToken, loginUserChangeEmailBody);
        changeUserClientResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User's changing Password after the User's Authorization")
    public void changeUserPasswordWithAuthorizationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ChangeUserClient changeUserClient = new ChangeUserClient();
        Response changeUserClientResponse = changeUserClient.changeUserPasswordWithAuthorization(authorisationToken, loginUserChangePasswordBody);
        changeUserClientResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User's changing Name after the User's Authorization")
    public void changeUserNameWithAuthorizationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ChangeUserClient changeUserClient = new ChangeUserClient();
        Response changeUserClientResponse = changeUserClient.changeUserNameWithAuthorization(authorisationToken, loginUserChangeNameBody);
        changeUserClientResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the User's changing data without the User's Authorization")
    public void changeUserDataWithoutAuthorizationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        ChangeUserClient changeUserClient = new ChangeUserClient();
        Response changeUserClientResponse = changeUserClient.changeUserDataWithoutAuthorization(loginUserChangeBody);
        changeUserClientResponse.then().assertThat().body("success", equalTo(false), "message", equalTo("You should be authorised"));
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
