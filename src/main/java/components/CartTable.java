package components;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import utils.Money;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class CartTable {
    private static final String PRICE = "[data-test='product-price']";
    private static final String QUANTITY = "[data-test='product-quantity']";
    private static final String LINE_PRICE = "[data-test='line-price']";
    private static final String REMOVE = ".btn-danger";

    private final ElementsCollection rows = $$("table tbody tr");

    private SelenideElement row(String title) {
        return rows.findBy(text(title));
    }

    @Step("Verify the cart has {expected} items")
    public CartTable shouldHaveRows(int expected) {
        rows.shouldHave(CollectionCondition.size(expected));
        return this;
    }

    @Step("Verify the cart contains '{title}'")
    public CartTable shouldContainProduct(String title) {
        row(title).shouldBe(visible);
        return this;
    }

    @Step("Read the unit price of '{title}'")
    public BigDecimal unitPrice(String title) {
        return Money.parse(row(title).$(PRICE).shouldBe(visible).getText());
    }

    @Step("Read the line total of '{title}'")
    public BigDecimal lineTotal(String title) {
        return Money.parse(row(title).$(LINE_PRICE).shouldBe(visible).getText());
    }

    @Step("Read the quantity of '{title}'")
    public String quantityOf(String title) {
        return row(title).$(QUANTITY).shouldBe(visible).getValue();
    }

    @Step("Change the quantity of '{title}' to {quantity}")
    public CartTable changeQuantity(String title, int quantity) {
        SelenideElement input = row(title).$(QUANTITY).shouldBe(visible);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(String.valueOf(quantity));
        input.pressTab();
        input.shouldHave(Condition.value(String.valueOf(quantity)));
        return this;
    }

    public CartTable shouldHaveLineTotal(String title, BigDecimal expected) {
        Allure.step("Verify the line total of '" + title + "' is " + Money.format(expected),
                () -> row(title).$(LINE_PRICE).shouldHave(partialText(Money.plain(expected))));
        return this;
    }

    @Step("Remove '{title}' from the cart")
    public CartTable remove(String title) {
        row(title).$(REMOVE).shouldBe(visible).click();
        return this;
    }
}