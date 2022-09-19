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
    DataOrder dataOrder = new DataOrder();
    private void testCodeAndBody(Order order) {
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
    int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - BLACK")
    public void testCreateOrderScooterWithColorBlack() {
        testCodeAndBody(dataOrder.dataTestCreateOrderScooterColor(List.of("BLACK")));
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - GREY")
    public void testCreateOrderScooterWithColorGrey() {
        testCodeAndBody(dataOrder.dataTestCreateOrderScooterColor(List.of("GREY")));
    }

    @Test
    @DisplayName("Тест возможности создания заказа с значение в поле color - BLACK и GREY")
    public void testCreateOrderScooterWithColorBlackAndGrey() {
        testCodeAndBody(dataOrder.dataTestCreateOrderScooterColor(List.of("BLACK", "GREY")));
    }

    @Test
    @DisplayName("Тест возможности создания заказа с пустым полем color")
    public void testCreateOrderScooterWithColorEmpty() {
        testCodeAndBody(dataOrder.dataTestCreateOrderScooterColor(List.of()));
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
        int statusCancel = responseCancel.statusCode();
        if (statusCancel != 200) {
            System.out.println("Не удалось отменить заказ");
        }
    }
}
