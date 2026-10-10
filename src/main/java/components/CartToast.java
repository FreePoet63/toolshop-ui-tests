package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class CartToast {
    private final SelenideElement alert = $x("//div[@role=\"alert\"]");

    @Step("Проверяем, что появилось уведомление о добавлении в корзину")
    public CartToast shouldShowAdded() {
        alert.shouldBe(visible).shouldHave(partialText("Product added to shopping cart."));
        return this;
    }
}