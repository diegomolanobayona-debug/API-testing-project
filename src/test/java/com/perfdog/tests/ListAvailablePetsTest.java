package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.equalTo;

/**
 * Valida la funcionalidad de listado de mascotas disponibles
 * (GET /pet/findByStatus?status=available).
 */
public class ListAvailablePetsTest {

    @BeforeClass
    public void setup() {
        ApiConfig.setup();
    }

    @Test
    public void listAvailablePets_shouldReturnOnlyAvailableStatus() {
        Response response = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus");

        System.out.println("Status code recibido del test ListAvailablePets: " + response.getStatusCode());

        response.then()
                .statusCode(200)
                .body("status", everyItem(equalTo("available")));
    }
}