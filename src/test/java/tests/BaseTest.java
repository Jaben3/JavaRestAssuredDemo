package tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    @BeforeTest
    public void setup(){
        RestAssured.baseURI = "https://simple-books-api.glitch.me";
    }
}
