package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Valida la funcionalidad de creación de una orden de compra
 * (POST /store/order).
 * Test independiente: obtiene un petId real desde /pet/findByStatus
 * dentro del propio test antes de generar la orden.
 */
public class CreateOrderTest {

    private long petId;

    @BeforeClass
    public void setup() {
        ApiConfig.setup();

        Response response = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus");

        petId = response.jsonPath().getLong("[0].id");
    }

    @Test
    public void createOrder_shouldReturnPlacedOrder() {
        String requestBody = "{"
                + "\"id\": 1,"
                + "\"petId\": " + petId + ","
                + "\"quantity\": 1,"
                + "\"shipDate\": \"2026-09-06T00:00:00.000Z\","
                + "\"status\": \"placed\","
                + "\"complete\": true"
                + "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200)
                .body("status", equalTo("placed"))
                .body("id", notNullValue())
                .extract().response();
        System.out.println("Status code recibido del test CreateOrder: " + response.getStatusCode());

        long returnedPetId = response.jsonPath().getLong("petId");
        Assert.assertEquals(returnedPetId, petId, "El petId de la orden no coincide");
    }
}