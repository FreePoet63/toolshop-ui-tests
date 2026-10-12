package pages;

import com.codeborne.selenide.SelenideElement;
import components.CartTable;
import components.Header;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Money;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CartPage {
    private final SelenideElement cartTotal = $("[data-test='cart-total']");
    private final SelenideElement emptyMessage = $x("//div[@role=\"alert\"]");

    private final CartTable cartTable = new CartTable();
    private final Header header = new Header();

    public CartTable table() {
        return cartTable;
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
        emptyMessage.shouldBe(visible);
        return this;
    }
}