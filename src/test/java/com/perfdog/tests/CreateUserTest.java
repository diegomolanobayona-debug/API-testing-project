package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Valida la funcionalidad de creación de usuario (POST /user).
 * Test independiente: genera su propio usuario con datos únicos.
 */
public class CreateUserTest {

    @BeforeClass
    public void setup() {
        ApiConfig.setup();
    }

    @Test
    public void createUser_shouldReturnSuccess() {
        String uniqueUsername = "perfdoguser" + System.currentTimeMillis();
        String requestBody = "{"
                + "\"id\": 1,"
                + "\"username\": \"" + uniqueUsername + "\","
                + "\"firstName\": \"Perf\","
                + "\"lastName\": \"Dog\","
                + "\"email\": \"" + uniqueUsername + "@perfdog.com\","
                + "\"password\": \"Test1234\","
                + "\"phone\": \"1234567890\","
                + "\"userStatus\": 1"
                + "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user");

        System.out.println("Status code recibido del test CreateUser: " + response.getStatusCode());
        System.out.println("Body recibido del test CreateUser: " + response.getBody().asString());

        response.then()
                .statusCode(200)
                .body("message", equalTo("1"));
    }
}