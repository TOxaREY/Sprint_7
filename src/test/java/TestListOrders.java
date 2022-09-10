import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты списка заказов")
public class TestListOrders {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест получения списка заказов")
    public void testOrdersList() {
        Response response =
                given().get("/api/v1/orders");
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
