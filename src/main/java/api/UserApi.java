package api;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.TestUser;

import static io.restassured.RestAssured.given;

public final class UserApi {
    private static final String API_URL = System.getProperty("api.url", "https://api.practicesoftwaretesting.com");
    private static final String COUNTRY_CODE = "US";
    private static final String REGISTER_PATH = "/users/register";

    private UserApi() {}

    public static TestUser register(TestUser user) {
        Allure.step("Create the user " + user.email() + " through the API", () -> {
            Response response = given()
                    .baseUri(API_URL)
                    .contentType(ContentType.JSON)
                    .body(toJson(user))
                    .post(REGISTER_PATH);

            Allure.step("POST " + REGISTER_PATH + " -> " + response.statusCode());

            if (response.statusCode() != 201) {
                Allure.addAttachment("Response body", "application/json", response.asString());
            }
            response.then().statusCode(201);
        });
        return user;
    }

    public static TestUser registerRandom() {
        return register(TestUser.random());
    }

    private static String toJson(TestUser user) {
        return """
                {
                  "first_name": "%s",
                  "last_name": "%s",
                  "address": {
                    "street": "%s",
                    "house_number": "%s",
                    "city": "%s",
                    "state": "%s",
                    "country": "%s",
                    "postal_code": "%s"
                  },
                  "phone": "%s",
                  "dob": "%s",
                  "password": "%s",
                  "email": "%s"
                }
                """.formatted(user.firstName(), esc(user.lastName()), esc(user.street()), esc(user.houseNumber()),
                esc(user.city()), esc(user.state()), COUNTRY_CODE, esc(user.postalCode()), esc(user.phone()),
                user.dateOfBirth(), esc(user.password()), esc(user.email()));
    }

    private static String esc(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}