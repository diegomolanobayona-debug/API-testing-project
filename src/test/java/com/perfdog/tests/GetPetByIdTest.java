package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;



import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Valida la funcionalidad de consulta de una mascota específica
 * (GET /pet/{petId}).
 * Test independiente: obtiene un petId real desde /pet/findByStatus
 * dentro del propio test, sin depender de otras clases.
 */
public class GetPetByIdTest {

    private long petId;

    @BeforeClass
    public void setup() {
        ApiConfig.setup();

        // Tomamos un petId real y válido desde la lista de disponibles
        Response response = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus");

        petId = response.jsonPath().getLong("[0].id");
    }

    @Test
    public void getPetById_shouldReturnMatchingPet() {
        Response response = given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .extract().response();

        long returnedId = response.jsonPath().getLong("id");
        Assert.assertEquals(returnedId, petId, "El id de la mascota consultada no coincide");
        Assert.assertNotNull(response.jsonPath().getString("name"), "El nombre no debería ser nulo");
    }
}