import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@DisplayName("Тесты создания курьера")
public class TestCreateCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест возможности создания курьера")
    @Description("Так же покрывает тест создания курьера с передачей всех обязательных полей")
    public void testCourierCanBeCreated() {
        File json = new File("src/test/resources/courierFull.json");
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Тест возможности создания двух одинаковых курьеров")
    @Description("Так же покрывает тест создания курьера с существующим логином")
    public void testCreatingTwoIdenticalCouriers() {
        File json = new File("src/test/resources/courierFull.json");
        given().header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(409)
                .and()
                .body("code", equalTo(409))
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Тест возможности создания курьера без поля login")
    public void testCourierCanBeCreatedWithoutLogin() {
        File json = new File("src/test/resources/courierWithoutLogin.json");
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("code", equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест возможности создания курьера без поля password")
    public void testCourierCanBeCreatedWithoutPassword() {
        File json = new File("src/test/resources/courierWithoutPassword.json");
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("code", equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест возможности создания курьера без поля firstName")
    public void testCourierCanBeCreatedWithoutFirstName() {
        File json = new File("src/test/resources/courierWithoutFirstName.json");
        Response response =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok", is(true));
    }

    @After
    public void tearDown() {
        File json = new File("src/test/resources/courierWithoutFirstName.json");
        Response responseId =
                given().header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        Integer statusId = responseId.statusCode();
        if (statusId == 200) {
            JsonPath jsonPathEvaluator = responseId.jsonPath();
            String id = jsonPathEvaluator.get("id").toString();
            Response responseDelete =
                    given().when()
                            .delete("/api/v1/courier/" + id);
            Integer statusDelete = responseDelete.statusCode();
            if (statusDelete != 200) {
                System.out.println("Не удалось удалить курьера");
            }
        }
    }
}
