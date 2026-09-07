package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Valida la funcionalidad de logout (GET /user/logout).
 * Test independiente: crea su propio usuario y hace login
 * antes de probar el logout, sin depender de otras clases.
 */
public class LogoutTest {

    private String username;
    private String password = "Test1234";

    @BeforeClass
    public void setup() {
        ApiConfig.setup();
        username = "perfdoguser" + System.currentTimeMillis();

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

        // Se crea el usuario
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user");

        // Se hace login con ese usuario
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
    }

    @Test
    public void logout_shouldReturnSuccess() {
        Response response = given()
                .when()
                .get("/user/logout");

        System.out.println("Status code recibido del test LogoutTest: " + response.getStatusCode());
        System.out.println("Body recibido del test LogoutTest: " + response.getBody().asString());

        response.then()
                .statusCode(200)
                .body("message", equalTo("ok"));
    }
}