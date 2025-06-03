package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HotelResourceTest {

    @Test
    @Order(1)
    @DisplayName("01 - Should return an empty list of check-in rooms initially")
    void shouldReturnEmptyListOfCheckInRoomsInitially() {
        given()
                .when()
                .log().ifValidationFails()
                .accept(ContentType.JSON)
                .get("/hotel/rooms?page=1&size=10")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @Order(2)
    @DisplayName("02 - Should successfully check in a room")
    void shouldSuccessfullyCheckInARoom() {
        given()
                .log().ifValidationFails()
                .when()
                .body("""
                        {
                            "number": "101",
                            "guest": {
                                "document": "123456789",
                                "name": "John Doe"
                            }
                        }
                        """)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .put("/hotel/rooms/")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("number", equalTo("101"))
                .body("guest.document", equalTo("123456789"))
                .body("guest.name", equalTo("John Doe"));
    }

    @Test
    @Order(3)
    @DisplayName("03 - Should return the list of checked-in rooms")
    void shouldReturnCheckedInRooms() {
        given()
                .log().ifValidationFails()
                .when()
                .contentType(ContentType.JSON)
                .get("/hotel/rooms?page=1&size=10")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    @Order(4)
    @DisplayName("04 - Should return checked-in rooms by guest document")
    void shouldReturnCheckedInRoomsByGuestDocument() {
        given()
                .log().ifValidationFails()
                .when()
                .contentType(ContentType.JSON)
                .get("/hotel/rooms/by-guest-document/{guestDocument}?page=1&size=10", "123456789")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasSize(1));

        given()
                .log().ifValidationFails()
                .when()
                .contentType(ContentType.JSON)
                .get("/hotel/rooms/by-guest-document/{guestDocument}?page=1&size=10", "2342342342")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @Order(5)
    @DisplayName("05 - Should successfully check out a room")
    void shouldSuccessfullyCheckOutARoom() {
        given()
                .log().ifValidationFails()
                .when()
                .delete("/hotel/rooms/{number}", "101")
                .then()
                .log().ifValidationFails()
                .statusCode(204);
    }

    @Test
    @Order(6)
    @DisplayName("06 - Should return an empty list of checked-in rooms after check out")
    void shouldReturnEmptyListOfCheckedInRooms() {
        given()
                .log().ifValidationFails()
                .when()
                .contentType(ContentType.JSON)
                .get("/hotel/rooms?page=1&size=10")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasSize(0));
    }
}