package com.perfdog.tests;

import com.perfdog.utils.ApiConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Caso negativo: valida que la API responda correctamente cuando se
 * consulta una mascota con un ID inexistente (bad path).
 */
public class GetPetByIdNotFoundTest {

    private static final long NON_EXISTENT_PET_ID = 999999999L;

    @BeforeClass
    public void setup() {
        ApiConfig.setup();
    }

    @Test
    public void getPetById_withInvalidId_shouldReturnNotFound() {
        Response response = given()
                .pathParam("petId", NON_EXISTENT_PET_ID)
                .when()
                .get("/pet/{petId}");

        int statusCode = response.getStatusCode();
        System.out.println("Status code recibido del test IdNotFound: " + statusCode);
        System.out.println("Body recibido del test IdNotFound: " + response.getBody().asString());

        Assert.assertEquals(statusCode, 404, "Se esperaba 404 para un ID inexistente");
    }
}