import api.client.*;
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

public class GetOrdersTest {

    private final String email = "vasya302@yandex.ru";
    private final String password = "1237897";
    private final String name = "maxim";
    private final String createUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}";
    private final String loginUserBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    private List<String> ingredients = new ArrayList<String>();
    private Order order = new Order(ingredients);
    private final String ingredientsBody = "{\"ingredients\": \"" + ingredients + "\"}";
    private String authorisationToken;

    @Before
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        TimeUnit.SECONDS.sleep(40);
    }

    @Test
    @DisplayName("Check the receipt of all Orders")
    public void checkReceiptTest() {
        GetOrdersClient getOrdersClient = new GetOrdersClient();
        Response getOrdersResponse = getOrdersClient.getOrders();
        getOrdersResponse.then().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check the receipt of User's Orders")
    public void checkUserReceiptTest() {
        UserClient userClient = new UserClient();
        Response createUserResponse = userClient.creationUser(createUserBody);
        String body = createUserResponse.body().asString();
        authorisationToken = Helper.parseAuthorisationToken(body);
        LoginClient login = new LoginClient();
        login.loginUser(loginUserBody);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6e");
        OrderClient orderClient = new OrderClient();
        orderClient.creationOrderWithoutAuthorisation(order);
        GetOrdersClient getOrdersClient = new GetOrdersClient();
        Response getOrdersResponse = getOrdersClient.getOrdersWithAuthorization(authorisationToken);
        getOrdersResponse.then().statusCode(200).and().assertThat().body("success", equalTo(true));
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
