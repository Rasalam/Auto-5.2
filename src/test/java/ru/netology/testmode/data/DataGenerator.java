package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto registeredUser) {
        given()
                .spec(requestSpec)
                .body(registeredUser) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.random().hex(5);
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = Registration.getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto();
            user.login = getRandomLogin();
            user.password = getRandomPassword();
            user.status = status;
            return user;
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class RegistrationDto {
        private String login;
        private String password;
        private String status;
    }
}
