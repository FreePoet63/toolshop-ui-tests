package test.cart;

import base.BaseTest;
import components.Header;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.ProductPage;
import utils.Money;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("practicesoftwaretesting.com")
@Feature("Cart")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class CartTest extends BaseTest {
    @Step("Add product #{index} из from the {categoryName} category to the cart, quantity {quantity}")
    private String addToCart(String categoryName, int index, int quantity) {
        var catalog = homePage.openCategory(categoryName);
        String title = catalog.products().titleAt(index);
        ProductPage productPage = catalog.products().openProduct(title);

        productPage.quantity()
                .increase(quantity - 1)
                .shouldHaveValue(String.valueOf(quantity));

        productPage.addToCart().toast().shouldShowAdded();
        return title;
    }

    private String addToCart(String categoryName, int index) {
        return addToCart(categoryName, index, 1);
    }

    @Test
    @Story("Product in the cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("An added product is show in the cart")
    public void shouldShowAddedProductInCart() {
        String title = addToCart("Hand Tools", 0);
        var cart = new Header().openCart();

        cart.table()
                .shouldHaveRows(1)
                .shouldContainProduct(title);
    }

    @Test
    @Story("Change quantity in the cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Changing the quantity recalculates the line total and the cart total")
    public void shouldRecalculateTotalAfterQuantityChange() {
        String title = addToCart("Hand Tools",0);
        var cart = new Header().openCart();
        BigDecimal unit = cart.table().shouldHaveRows(1).unitPrice(title);
        BigDecimal expected = unit.multiply(BigDecimal.valueOf(3));

        cart.table().changeQuantity(title, 3);

        cart.table().shouldHaveLineTotal(title, expected);
        cart.shouldHaveTotal(expected);
        Allure.step("Verify the quantity field shows 3",
                () -> assertThat(cart.table().quantityOf(title)).isEqualTo("3"));
    }

    @Test
    @Story("Remove from the cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Removing the only product leaves the cart empty")
    public void shouldRemoveProductFormCart() {
        String title = addToCart("Hand Tools",0);
        var cart = new Header().openCart();

        cart.table().shouldContainProduct(title).remove(title);

        cart.shouldBeEmpty();
    }

    @Test
    @Story("Cart total")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The cart total equals the sum of its line totals when the cart has different products")
    public void shouldCalculateTotalForTwoProducts() {
        String first = addToCart("Hand Tools",0);
        open("/");
        String second = addToCart("Power Tools", 0);

        var cart = new Header().openCart();
        cart.table().shouldHaveRows(2);

        BigDecimal expected = cart.table().lineTotal(first).add(cart.table().lineTotal(second));
        BigDecimal actual = cart.total();

        Allure.step("Verify the total equals the sum of line totals (" + Money.format(actual) + " = " +
                        Money.format(expected) + ")",
                () -> assertThat(actual).isEqualByComparingTo(expected));
    }

    @Test
    @Story("Cart total")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The cart total equals the unit price multiplied by the chosen quantity")
    public void shouldCalculateTotalForMultipleIdenticalProducts() {
        String title = addToCart("Hand Tools", 0, 2);
        var cart = new Header().openCart();

        BigDecimal unit = cart.table().shouldHaveRows(1).unitPrice(title);
        BigDecimal expected = unit.multiply(BigDecimal.valueOf(2));
        BigDecimal actual = cart.total();

        Allure.step("Verify the cart quantity is 2",
                () -> assertThat(cart.table().quantityOf(title)).isEqualTo("2"));
        Allure.step("Verify the total equals the unit price x 2 (" + Money.format(actual) + " = " +
                        Money.format(expected) + ")",
                () -> assertThat(actual).isEqualByComparingTo(expected));
    }
}