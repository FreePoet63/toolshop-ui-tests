package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.PaymentData;
import model.PaymentMethod;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PaymentForm {
    private final SelenideElement methodSelect = $("[data-test='payment-method']");
    private final SelenideElement confirmButton = $("[data-test='finish']");

    private final FormAlert alert = new FormAlert();

    public FormField field(String id) {
        return new FormField(id);
    }

    @Step("Select the payment method '{method}'")
    public PaymentForm selectMethod(PaymentMethod method) {
        methodSelect.shouldBe(visible).selectOptionContainingText(method.label());
        return this;
    }

    public PaymentForm fill(PaymentData data) {
        Allure.step("Fill in the payment details: " + data.method().label(), () -> {
            selectMethod(data.method());
            data.fields().forEach((id, value) -> field(id).type(value));
        });
        return this;
    }

    public PaymentForm shouldShowError(String expected) {
        alert.shouldHaveText(expected);
        return this;
    }

    @Step("Confirm the payment")
    public PaymentForm confirm() {
        confirmButton.shouldBe(visible).click();
        return this;
    }
}