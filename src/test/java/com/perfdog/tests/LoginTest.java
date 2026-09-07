package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Valida la funcionalidad de login (GET /user/login).
 * Test independiente: crea su propio usuario antes de intentar el login,
 * para no depender de datos generados en otro test.
 */
public class LoginTest {

    private String username;
    private String password = "Test1234";

    @BeforeClass
    public void setup() {
        ApiConfig.setup();
        username = "perfdogusr" + System.currentTimeMillis();

        String requestBody = "{"
                + "\"id\": 1,"
                + "\"username\": \"" + username + "\","
                + "\"firstName\": \"Perf\","
                + "\"lastName\": \"Dog\","
                + "\"email\": \"" + username + "@perfdog.com\","
                + "\"password\": \"" + password + "\","
                + "\"phone\": \"1234567890\","
                + "\"userStatus\": 1"
                + "}";

        // Se crea el usuario que este test usará para loguearse
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user");
    }

    @Test
    public void login_withValidUser_shouldReturnSuccess() {
        Response response = given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");

        System.out.println("Status code recibido del test LoginTest: " + response.getStatusCode());
        System.out.println("Body recibido del test LoginTest: " + response.getBody().asString());

        response.then()
                .statusCode(200)
                .body("message", containsString("logged in user session"));
    }
}