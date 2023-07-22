package tests;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

public class OrdersTest extends BaseTest{

    @Test
    @Parameters({"accessToken"})
    public void orderBook(String accessToken, ITestContext context) {
        Faker faker = new Faker();
        int number = faker.number().numberBetween(1, 6);
        String randomNumber = Integer.toString(number);
        String randomName = faker.name().fullName();

        JSONObject body = new JSONObject();
        body.put("bookId", randomNumber);
        body.put("customerName", randomName);

        String orderId = given()
                .auth().oauth2(accessToken)
                .body(body.toString())
                .contentType("application/json")
        .when()
                .post("/orders")
        .then()
                .log().all()
                .statusCode(201)
                .extract().jsonPath().get("orderId");

        context.setAttribute("bookId", randomNumber);
        context.setAttribute("customerName", randomName);
        context.setAttribute("orderId", orderId);
    }

    @Test(dependsOnMethods = {"orderBook"})
    @Parameters({"accessToken"})
    public void getAnOrder(String accessToken, ITestContext context) {
        String orderId = (String) context.getAttribute("orderId");

        given()
                .auth().oauth2(accessToken)
                .pathParam("orderId", orderId)
        .when()
                .get("/orders/{orderId}")
        .then()
                .statusCode(200)
                .log().all();
    }


    @Test(dependsOnMethods = {"getAnOrder"})
    @Parameters({"accessToken"})
    public void updateAnOrder(String accessToken, ITestContext context) {
        String orderId = (String) context.getAttribute("orderId");

        Faker faker = new Faker();
        Name name = faker.name();

        JSONObject body = new JSONObject();
        body.put("customerName", name.fullName());

        given()
                .auth().oauth2(accessToken)
                .pathParam("orderId", orderId)
                .body(body.toString())
                .contentType("application/json")
        .when()
                .patch("/orders/{orderId}")
        .then()
                .statusCode(204)
                .log().all();
    }

    @Test(dependsOnMethods = {"updateAnOrder"})
    @Parameters({"accessToken"})
    public void getAllOrders(String accessToken) {
        given()
                .auth().oauth2(accessToken)
        .when()
                .get("/orders")
        .then()
                .statusCode(200)
                .log().all();
    }


    @Test(dependsOnMethods = {"getAllOrders"}, alwaysRun = true)
    @Parameters({"accessToken"})
    public void deleteAnOrder(String accessToken, ITestContext context) {
        String orderId = (String) context.getAttribute("orderId");

        given()
                .auth().oauth2(accessToken)
                .pathParam("orderId", orderId)
        .when()
                .delete("/orders/{orderId}")
        .then()
                .statusCode(204)
                .log().all();
    }



    @Test(groups = {"negative testing"})
    public void orderBookUnauthorized() {
        given()
        .when()
                .post("/orders")
        .then()
                .log().all()
                .statusLine("HTTP/1.1 401 Unauthorized");
    }

}
