package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartToast {
    private final SelenideElement alert = $("[role=alert]");

    @Step("Verify the 'added to cart' notification is shown")
    public CartToast shouldShowAdded() {
        alert.shouldBe(visible).shouldHave(partialText("Product added to shopping cart."));
        return this;
    }

    public CartToast shouldBeEmpty() {
        alert.shouldBe(visible);
        return this;
    }
}