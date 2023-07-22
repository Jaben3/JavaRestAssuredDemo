package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;

// TODO: Get a list of books limited to certain number

public class BookStoreTest extends BaseTest {

    @Test
    public void getListOfBooks() {
        given()
        .when()
                .get("/books")
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void getListOfBooksBadQueryParam() {
        given()
                .queryParam("type", "fantasy")
        .when()
                .get("/books")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void getListOfBooksFilteredByKeyword() {
        String keyword = "fiction";

        ArrayList<String> booksType = given()
                .queryParam("type", keyword)
        .when()
                .get("/books")
        .then()
                .log().all()
                .statusCode(200)
                .extract().jsonPath().get("type");

        for (String bookType : booksType) {
            Assert.assertEquals(bookType, keyword);
        }
    }

    @Test
    public void getSingleBook() {
        Faker faker = new Faker();
        int randomNumber = faker.number().numberBetween(1, 6);

        int id = given()
                .pathParam("bookId", randomNumber)
        .when()
                .get("/books/{bookId}")
        .then()
                .log().all()
                .statusCode(200)
                .extract().jsonPath().get("id");

        Assert.assertEquals(id, randomNumber);

    }

    @Test
    public void getSingleBookThatNotExist() {

        Response response = given()
                .pathParam("bookId", 1000)
        .when()
                .get("/books/{bookId}")
        .then()
                .log().all()
                .statusCode(404)
                .extract().response();

        Assert.assertTrue(response.body().asString().contains("No book with id"));
    }
}
