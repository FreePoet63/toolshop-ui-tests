package pages;

import com.codeborne.selenide.SelenideElement;
import components.CartTable;
import components.CartToast;
import components.Header;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Money;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartPage {
    private final SelenideElement cartTotal = $("[data-test='cart-total']");
    private final SelenideElement proceedButton = $("[data-test='proceed-1']");

    private final CartTable cartTable = new CartTable();
    private final CartToast cartToast = new CartToast();
    private final Header header = new Header();

    public CartTable table() {
        return cartTable;
    }

    public CartToast cartToast() {
        return cartToast;
    }

    public Header header() {
        return header;
    }

    @Step("Read the cart total")
    public BigDecimal total() {
        return Money.parse(cartTotal.shouldBe(visible).getText());
    }

    public CartPage shouldHaveTotal(BigDecimal expected) {
        Allure.step("Verify the cart total is " + Money.format(expected),
                () -> cartTotal.shouldHave(partialText(Money.plain(expected))));
        return this;
    }

    @Step("Verify the cart is empty")
    public CartPage shouldBeEmpty() {
        cartToast().shouldBeEmpty();
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        proceedButton.shouldBe(visible).click();
        return new CheckoutPage();
    }
}