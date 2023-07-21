package tests;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;

public class StatusTests extends BaseTest{

    @Test
    public void testAPIStatus(){
        given()
        .when()
                .get("/status")
        .then()
                .log().all()
                .statusCode(200);
    }
}
