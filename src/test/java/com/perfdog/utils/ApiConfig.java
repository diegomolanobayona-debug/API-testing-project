package com.perfdog.utils;

import io.restassured.RestAssured;
/**
 * Configuración común para todos los tests de la API de Petstore.
 * Centraliza la URL base para no repetirla en cada clase de test.
 */
public class ApiConfig {

    public static final String BASE_URI = "https://petstore.swagger.io/v2";
    /**
     * Establece la URI base de RestAssured. Debe llamarse antes de
     * cualquier request dentro de cada test (ej. en un método @BeforeClass).
     */
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }
}