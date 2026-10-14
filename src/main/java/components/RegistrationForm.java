package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.TestUser;

import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationForm {

    public enum Field {
        FIRST_NAME("first-name"),
        LAST_NAME("last-name"),
        DATE_OF_BIRTH("dob"),
        COUNTRY("country"),
        POSTAL_CODE("postal_code"),
        HOUSE_NUMBER("house_number"),
        STREET("street"),
        CITY("city"),
        STATE("state"),
        PHONE("phone"),
        EMAIL("email"),
        PASSWORD("password");

        private final String id;
        Field (String id) {
            this.id = id;
        }
        public String id() {
            return id;
        }
    }

    private static final DateTimeFormatter DOB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final SelenideElement countrySelect = $("[data-test='country']");
    private final SelenideElement submitButton = $("[data-test='register-submit']");

    public FormField field(Field field) {
        return new FormField(field.id);
    }

    public RegistrationForm fill(TestUser user) {
        Allure.step("Fill in the register form for " + user.email(), () -> {
            field(Field.FIRST_NAME).type(user.firstName());
            field(Field.LAST_NAME).type(user.lastName());
            field(Field.DATE_OF_BIRTH).setValueByScript(DOB_FORMAT.format(user.dateOfBirth()));
            selectCountry(user.country());
            field(Field.POSTAL_CODE).type(user.postalCode());
            field(Field.HOUSE_NUMBER).type(user.houseNumber());
            field(Field.STREET).type(user.street());
            field(Field.CITY).type(user.city());
            field(Field.STATE).type(user.state());
            field(Field.PHONE).type(user.phone());
            field(Field.EMAIL).type(user.email());
            field(Field.PASSWORD).type(user.password());
        });
        return this;
    }

    @Step("Select country '{country}'")
    public RegistrationForm selectCountry(String country) {
        countrySelect.shouldBe(visible).selectOption(country);
        return this;
    }

    @Step("Submit the registration form")
    public RegistrationForm submit() {
        submitButton.shouldBe(visible).click();
        return this;
    }
}