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
    @Story("Открытие карточки товара")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Карточка открывается из каталога, название совпадает с названием в списке")
    public void shouldOpenProductFromCatalog() {
        var catalog = homePage.openCategory("Hand Tools");
        String title = catalog.products().firstTitle();

        var productPage = catalog.products().openProduct(title);
        Allure.step("Проверяем, что название в карточке совпадает с названием в каталоге «" + title + "»)",
                () -> assertThat(productPage.name()).isEqualTo(title));
    }

    @Test
    @Story("Содержимое карточки товара")
    @Severity(SeverityLevel.NORMAL)
    @Description("В карточке товара отображаются цена и описание")
    public void shouldShowPriceAndDescription() {
        var catalog = homePage.openCategory("Hand Tools");
        var productPage = catalog.products().openProduct(catalog.products().firstTitle());

        String price = productPage.price();
        String description = productPage.description();
        Allure.step("Проверяем, что в цене есть число (" + price + ")",
                () -> assertThat(price).containsPattern("\\d"));
        Allure.step("Проверяем, что описание не пустое",
                () -> assertThat(description).isNotBlank());
    }

    @Test
    @Story("Изменение количества")
    @Severity(SeverityLevel.NORMAL)
    @Description("Кнопки + и - меняют количество товара в карточке")
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
    @Story("Добавление в корзину")
    @Severity(SeverityLevel.CRITICAL)
    @Description("После добавления товара появляется уведомление, а в значке корзины число 1")
    public void shouldAddProductToCart() {
        var catalog = homePage.openCategory("Hand Tools");
        var productPage = catalog.products().openProduct(catalog.products().firstTitle());

        productPage.addToCart();

        productPage.toast().shouldShowAdded();
        productPage.header().shouldHaveCartCount("1");
    }
}