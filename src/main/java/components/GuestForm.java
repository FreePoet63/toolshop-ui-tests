package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.TestUser;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static components.Elements.text;

public class GuestForm {
    private final SelenideElement submitButton = $("[data-test='guest-submit']");

    private static final String EMAIL = "guest-email";
    private static final String FIRST_NAME = "guest-first-name";
    private static final String LAST_NAME = "guest-last-name";
    private static final String TEXT_LINK = "Continue as Guest";

    @Step("Verify the option to continue as a guest is offered")
    public GuestForm shouldBeOffered() {
        text(TEXT_LINK).shouldBe(visible);
        return this;
    }

    @Step("Open the guest form")
    public GuestForm open() {
        text(TEXT_LINK).shouldBe(visible).click();
        return this;
    }

    public GuestForm fill(TestUser guest) {
        Allure.step("Fill in the guest details for " + guest.email(), () -> {
            new FormField(EMAIL).type(guest.email());
            new FormField(FIRST_NAME).type(guest.firstName());
            new FormField(LAST_NAME).type(guest.lastName());
        });
        return this;
    }

    @Step("Submit the guest details")
    public GuestForm submit() {
        submitButton.shouldBe(visible).click();
        return this;
    }
}