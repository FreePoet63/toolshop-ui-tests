package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.TestUser;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class BillingAddressForm {
    private final SelenideElement proceedButton = $("[data-test='proceed-3']");

    public FormField field(Field field) {
        return new FormField(field.id());
    }

    public BillingAddressForm fill(TestUser user) {
        Allure.step("Fill in the billing address for " + user.email(), () -> {
            field(Field.COUNTRY).select(user.country());
            field(Field.POSTAL_CODE).type(user.postalCode());
            field(Field.HOUSE_NUMBER).type(user.houseNumber());
            field(Field.STREET).type(user.street());
            field(Field.CITY).type(user.city());
            field(Field.STATE).type(user.state());
        });
        return this;
    }

    public BillingAddressForm shouldBePrefilledFrom(TestUser user) {
        Allure.step("Verify the billing address is prefilled from the profile of " + user.email(), () -> {
            field(Field.POSTAL_CODE).shouldHaveValue(user.postalCode());
            field(Field.HOUSE_NUMBER).shouldHaveValue(user.houseNumber());
            field(Field.STREET).shouldHaveValue(user.street());
            field(Field.CITY).shouldHaveValue(user.city());
            field(Field.STATE).shouldHaveValue(user.state());
        });
        return this;
    }

    @Step("Continue to the payment step")
    public BillingAddressForm submit() {
        proceedButton.shouldBe(visible).click();
        return this;
    }
}