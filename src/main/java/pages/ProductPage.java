package pages;

import com.codeborne.selenide.SelenideElement;
import components.CartToast;
import components.Header;
import components.QuantityControl;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProductPage {
    private final SelenideElement productName = $x("//h1[@data-test=\"product-name\"]");
    private final SelenideElement price = $(".price-section");
    private final SelenideElement description = $("#description");
    private final SelenideElement addToCartButton = $("#btn-add-to-cart");

    private final QuantityControl quantityControl = new QuantityControl();
    private final CartToast cartToast = new CartToast();
    private final Header header = new Header();

    public QuantityControl quantity() {
        return quantityControl;
    }

    public CartToast toast() {
        return cartToast;
    }

    public Header header() {
        return header;
    }

    @Step("Получаем название товара")
    public String name() {
        return productName.shouldBe(visible).getText();
    }

    @Step("Получаем цену товара")
    public String price() {
        return price.shouldBe(visible).getText();
    }

    @Step("Получаем описание товара")
    public String description() {
        return description.shouldBe(visible).getText();
    }

    @Step("Добавляем товар в корзину")
    public ProductPage addToCart() {
        addToCartButton.shouldBe(visible).click();
        return this;
    }
}