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
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты логина курьера")
public class TestCourierLogin {
    private Response response(File json) {
        return given().header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        File json = new File("src/test/resources/courierFull.json");
        given().header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Тест возможности авторизоваться курьеру")
    @Description("Так же покрывает тест авторизации курьера с передачей всех обязательных полей")
    public void testCourierCanBeAuthorized() {
        File json = new File("src/test/resources/courierWithoutFirstName.json");
        response(json).then().assertThat()
                .statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Тест возможности авторизации курьера без поля login")
    public void testCourierCanBeAuthorizationWithoutLogin() {
        File json = new File("src/test/resources/courierWithoutFirstNameAndLogin.json");
        response(json).then().assertThat()
                .statusCode(400)
                .and()
                .body("code", equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест возможности авторизации курьера без поля password")
    public void testCourierCanBeAuthorizationWithoutPassword() {
        File json = new File("src/test/resources/courierWithoutFirstNameAndPassword.json");
        response(json).then().assertThat()
                .statusCode(400)
                .and()
                .body("code", equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест возможности авторизации курьера c неправильным полем login")
    @Description("Так же покрывает тест авторизации курьера с несуществующим логином")
    public void testCourierCanBeAuthorizationWithWrongLogin() {
        File json = new File("src/test/resources/courierWrongLogin.json");
        response(json).then().assertThat()
                .statusCode(404)
                .and()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест возможности авторизации курьера c неправильным полем password")
    public void testCourierCanBeAuthorizationWithWrongPassword() {
        File json = new File("src/test/resources/courierWrongPassword.json");
        response(json).then().assertThat()
                .statusCode(404)
                .and()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
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
        int statusId = responseId.statusCode();
        if (statusId == 200) {
            JsonPath jsonPathEvaluator = responseId.jsonPath();
            String id = jsonPathEvaluator.get("id").toString();
            Response responseDelete =
                    given().when()
                            .delete("/api/v1/courier/" + id);
            int statusDelete = responseDelete.statusCode();
            if (statusDelete != 200) {
                System.out.println("Не удалось удалить курьера");
            }
        }
    }
}
