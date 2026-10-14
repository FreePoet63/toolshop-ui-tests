package model;

import java.time.LocalDate;

public record TestUser (
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String country,
    String postalCode,
    String houseNumber,
    String street,
    String city,
    String state,
    String phone,
    String email,
    String password) {

    public static TestUser random() {
        return new TestUser(
                "John",
                "Doe",
                LocalDate.of(1990, 5, 15),
                "United States of America (the)",
                "12345",
                "4",
                "Test Street 1",
                "Springfield",
                "Illinois",
                "1234567890",
                "user" + System.currentTimeMillis() + "@example.com",
                "SrtOng!PasswOrd#2026");
    }

    public TestUser withEmail(String email) {
        return new TestUser(firstName, lastName, dateOfBirth, country, postalCode, houseNumber, street, city, state,
                phone, email, password);
    }

    public TestUser withPassword(String password) {
        return new TestUser(firstName, lastName, dateOfBirth, country, postalCode, houseNumber, street, city, state,
                phone, email, password);
    }

    @Override
    public String toString() {
        return "TestUser[firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", password=***]";
    }
}