package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class QuantityControl {
    private final SelenideElement input = $("#quantity-input");
    private final SelenideElement increaseButton = $("#btn-increase-quantity");
    private final SelenideElement decreaseButton = $("#btn-decrease-quantity");

    @Step("Increase quantity")
    public QuantityControl increase() {
        increaseButton.shouldBe(visible).click();
        return this;
    }

    @Step("Increase the quantity {times}")
    public QuantityControl increase(int time) {
        for (int i = 0; i < time; i++) {
            increaseButton.shouldBe(visible).click();
        }
        return this;
    }

    @Step("Decrease quantity")
    public QuantityControl decrease() {
        decreaseButton.shouldBe(visible).click();
        return this;
    }

    @Step("Verify the quantity is {expected}")
    public QuantityControl shouldHaveValue(String expected) {
        input.shouldHave(value(expected));
        return this;
    }
}