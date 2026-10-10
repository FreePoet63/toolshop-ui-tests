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

    @Step("Увеличиваем количество")
    public QuantityControl increase() {
        increaseButton.shouldBe(visible).click();
        return this;
    }

    @Step("Уменьшаем количество")
    public QuantityControl decrease() {
        decreaseButton.shouldBe(visible).click();
        return this;
    }

    @Step("Проверяем, что количество равно {expected}")
    public QuantityControl shouldHaveValue(String expected) {
        input.shouldHave(value(expected));
        return this;
    }
}