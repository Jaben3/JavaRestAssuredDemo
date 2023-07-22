package tests;

import static io.restassured.RestAssured.*;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

public class RegisterNewClientTest extends BaseTest{

    @Test
    public void registerApi(){
        JSONObject body = new JSONObject();
        body.put("clientName", "TestJB2");
        body.put("clientEmail", "TestJB2@example.com");

        given()
                .body(body.toString())
                .contentType("application/json")
        .when()
                .post("/api-clients")
        .then()
                .log().all()
                .statusCode(201);


        given()
                .body(body.toString())
                .contentType("application/json")
        .when()
                .post("/api-clients")
        .then()
                .log().all()
                .statusCode(409);
    }
}
