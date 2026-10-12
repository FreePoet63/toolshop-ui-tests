package test.product;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("practicesoftwaretesting.com")
@Feature("Product")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class ProductTest extends BaseTest {
    @Test
    @Story("Open a product page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The product page opens from the catalog and its name matches the catalog title")
    public void shouldOpenProductFromCatalog() {
        var catalog = homePage.openCategory("Hand Tools");
        String title = catalog.products().firstTitle();

        var productPage = catalog.products().openProduct(title);
        Allure.step("Verify the productname matches the catalog title ('" + title + "')",
                () -> assertThat(productPage.name()).isEqualTo(title));
    }

    @Test
    @Story("Product page content")
    @Severity(SeverityLevel.NORMAL)
    @Description("The product page shows a price and a description")
    public void shouldShowPriceAndDescription() {
        var catalog = homePage.openCategory("Hand Tools");
        var productPage = catalog.products().openProduct(catalog.products().firstTitle());

        String price = productPage.price();
        String description = productPage.description();
        Allure.step("Verify the price contains a number (" + price + ")",
                () -> assertThat(price).containsPattern("\\d"));
        Allure.step("Verify the description is not empty",
                () -> assertThat(description).isNotBlank());
    }

    @Test
    @Story("Change quantity")
    @Severity(SeverityLevel.NORMAL)
    @Description("The + and - buttons change the product quantity")
    public void shouldChangeQuantity() {
        var catalog = homePage.openCategory("Hand Tools");
        var productPage = catalog.products().openProduct(catalog.products().firstTitle());

        productPage.quantity()
                .shouldHaveValue("1")
                .increase()
                .increase()
                .shouldHaveValue("3")
                .decrease()
                .shouldHaveValue("2");
    }

    @Test
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After adding a products a notification appears and the cart badge shows 1")
    public void shouldAddProductToCart() {
        var catalog = homePage.openCategory("Hand Tools");
        var productPage = catalog.products().openProduct(catalog.products().firstTitle());

        productPage.addToCart();

        productPage.toast().shouldShowAdded();
        productPage.header().shouldHaveCartCount("1");
    }
}