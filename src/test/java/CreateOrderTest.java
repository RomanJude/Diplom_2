import api.client.LoginClient;
import api.client.Order;
import api.client.OrderClient;
import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

    private final String email = "vasya2700@yandex.ru";
    private final String password = "1237897";
    private final String name = "maxim";
    private final String createUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String loginUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    private String authorisationToken;

    private List<String> ingredients = new ArrayList<String>();
    private Order order = new Order(ingredients);
    private final String ingredientsBody = "{\"ingredients\": \"" + ingredients + "\"}";

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        TimeUnit.SECONDS.sleep(40);
    }

    @Test
    @DisplayName("Check the Order's creation without the User's Authorisation with wright Ingredient's hash")
    public void creationTheOrderTest() {
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6e");
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the Order's creation without the User's Authorisation without Ingredients")
    public void creationTheOrderWithoutIngredientsTest() {
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(400);
    }

    @Test
    @DisplayName("Check the Order's creation without the User's Authorisation with space instead if Ingredients hash")
    public void creationTheOrderWithSpaceHashTest() {
        ingredients.add(" ");
        ingredients.add(" ");
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(500);
    }

    @Test
    @DisplayName("Check the Order's creation without the User's Authorisation with wrong Ingredient's hash") // часто появляется 429 ошибка: слишком много запросов
    public void creationTheOrderWithWrongHashTest() {
        ingredients.add("61c0c5a71d1f82001bdaaa6yy");
        ingredients.add("61c0c5a71d1f82001bdaaa6zz");
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(500);
    }

    @Test
    @DisplayName("Check the Order's creation with the User's Authorisation with wright ingredient's hash")
    public void creationTheOrderWithAuthorisationTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6e");
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the Order's creation with the User's Authorisation without ingredients hash")
    public void creationTheOrderWithAuthorisationWithoutIngredientsTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(400);
    }

    @Test
    @DisplayName("Check the Order's creation with the User's Authorisation with space instead if Ingredients hash")
    public void creationTheOrderWithAuthorisationWithSpaceHashTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ingredients.add(" ");
        ingredients.add(" ");
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(500);
    }

    @Test
    @DisplayName("Check the Order's creation with the User's Authorisation with wrong ingredient's hash")
    public void creationTheOrderWithAuthorisationWithWrongHashTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        ingredients.add("dkdfskfjdlja");
        ingredients.add("jskjfkfjdkdjkjsdj");
        login.loginUser(loginUserBody);
        OrderClient orderClient = new OrderClient();
        Response createOrderBodyResponse = orderClient.creationOrderWithoutAuthorisation(order);
        createOrderBodyResponse.then().statusCode(500);
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