import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты создания заказа")
public class TestCreateOrder {

    Integer track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - BLACK")
    public void testCreateOrderScooterWithColorBlack() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Алтайская 45-89",
                10,
                "89771234567",
                2,
                "2022-09-15",
                "3-й подъезд",
                List.of("BLACK"));
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        JsonPath jsonPathEvaluator = response.jsonPath();
        track = jsonPathEvaluator.get("track");
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - GREY")
    public void testCreateOrderScooterWithColorGrey() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Алтайская 45-89",
                10,
                "89771234567",
                2,
                "2022-09-15",
                "3-й подъезд",
                List.of("GREY"));
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        JsonPath jsonPathEvaluator = response.jsonPath();
        track = jsonPathEvaluator.get("track");
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - BLACK и GREY")
    public void testCreateOrderScooterWithColorBlackAndGrey() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Алтайская 45-89",
                10,
                "89771234567",
                2,
                "2022-09-15",
                "3-й подъезд",
                List.of("BLACK", "GREY"));
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        JsonPath jsonPathEvaluator = response.jsonPath();
        track = jsonPathEvaluator.get("track");
    }

    @Test
    @DisplayName("Тест возможности создания заказа с пустым полем color")
    public void testCreateOrderScooterWithColorEmpty() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Алтайская 45-89",
                10,
                "89771234567",
                2,
                "2022-09-15",
                "3-й подъезд",
                List.of());
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        JsonPath jsonPathEvaluator = response.jsonPath();
        track = jsonPathEvaluator.get("track");
    }

    @After
    public void tearDown() {
        String json = "{\"track\": " + track + "}";
        Response responseCancel =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .put("/api/v1/orders/cancel");
        Integer statusCancel = responseCancel.statusCode();
        if (statusCancel != 200) {
            System.out.println("Не удалось отменить заказ");
        }
    }
}
