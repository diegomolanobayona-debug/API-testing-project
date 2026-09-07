# API Tests para PerfDog

La idea es simular el flujo de un usuario
en la tienda de mascotas "PerfDog": crear cuenta, loguearse, ver el catálogo,
consultar una mascota, comprarla y cerrar sesión y ver la respuesta de la API 
a estas peticiones usando la suite de pruebas con **RestAssured** + **TestNG**.

## ¿Qué es esto y por qué así?

Cada test simula una acción real de un cliente hablando **directamente con el servidor**
de la tienda (sin pasar por ninguna pantalla/navegador, a diferencia de un test de UI).

Analogía: la API es un restaurante con varias "ventanillas" (endpoints), cada una
para un tipo de pedido específico:

| Ventanilla (endpoint)         | Qué le pides                          |
|--------------------------------|----------------------------------------|
| `POST /user`                  | "Quiero abrir una cuenta nueva"       |
| `GET /user/login`             | "Soy fulano, déjame entrar"           |
| `GET /pet/findByStatus`       | "¿Qué hay disponible hoy?"            |
| `GET /pet/{petId}`            | "Quiero ver el detalle de este plato" |
| `POST /store/order`           | "Quiero pedir este plato"             |
| `GET /user/logout`            | "Ya me voy, cierro cuenta"            |

Cada test valida que, al pedir eso, la API responda con lo que se supone que debe
responder (código de estado correcto + contenido esperado en el body).

## Estructura del proyecto

```
src/test/java/com/perfdog/
├── utils/
│   └── ApiConfig.java              → configuración común (URL base de la API)
└── tests/
    ├── CreateUserTest.java         → POST /user
    ├── LoginTest.java              → GET /user/login
    ├── ListAvailablePetsTest.java  → GET /pet/findByStatus?status=available
    ├── GetPetByIdTest.java         → GET /pet/{petId}
    ├── CreateOrderTest.java        → POST /store/order
    └── LogoutTest.java             → GET /user/logout

testng.xml   → orquesta la suite completa (los 6 tests juntos)
pom.xml      → dependencias (RestAssured, TestNG, Hamcrest) vía Maven
```
## Qué valida cada test

- **CreateUserTest**: crea un usuario con username único (timestamp) y valida que
  la API confirme la creación (200).
- **LoginTest**: crea su propio usuario y valida que el login con credenciales
  válidas responda 200 con mensaje de sesión iniciada.
- **ListAvailablePetsTest**: consulta el catálogo filtrado por `status=available`
  y valida que **todas** las mascotas devueltas tengan ese status.
- **GetPetByIdTest**: toma un `petId` real desde el catálogo y valida que el
  detalle consultado corresponda a esa misma mascota.
- **CreateOrderTest**: toma un `petId` real y crea una orden, validando que quede
  en estado `"placed"` y asociada al pet correcto.
- **LogoutTest**: crea usuario, hace login y valida que el logout responda 200.
- **GetPetByIdNotFoundTest**: valida el camino negativo — al consultar un `petId`
  inexistente, la API debe responder `404` con mensaje de error, no un `200` falso.

Cada test es **independiente**: obtiene sus propios datos (usuario, petId) dentro
de su `@BeforeClass`, sin depender de lo que haya hecho otro test.

## Cómo correrlo

- Desde IntelliJ: clic derecho en `testng.xml` → **Run**.
- Desde terminal: `mvn clean test`

## Aprendizajes / errores corregidos en el camino

- **`int` vs `long` en los IDs**: la API de Petstore devuelve IDs que a veces son
  pequeños y a veces enormes (no caben en un `int` de Java). Forzar la conversión
  a `int` (`(int) petId`) producía un *overflow*: el número se desbordaba y se
  convertía en otro valor (incluso negativo), haciendo fallar el test aunque la
  API respondiera correctamente. Solución: mantener todo en `long` de principio
  a fin, y usar `jsonPath().getLong(...)` en vez de comparar directamente con
  `equalTo()`, ya que ese método normaliza el tipo sin importar si el JSON trajo
  el número como `Integer` o `Long`.
- Este bug era **intermitente** (test *flaky*): pasaba o fallaba dependiendo de
  qué `petId` le tocara al azar en cada corrida, sin que el código cambiara.

## Herramientas usadas

- Java 8+
- Maven
- RestAssured 5.4.0
- TestNG 7.10.2
- Hamcrest 2.2