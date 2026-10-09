package test.search;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("practicesoftwaretesting.com")
@Feature("Search")
public class SearchTest extends BaseTest {

    @Test
    @Story("Поиск существующего товара")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Поиск по полному названию товара возвращает товар с этим названием")
    public void shouldFindExistingProduct() {
        var result = homePage.search("Hammer");

        result.products().shouldHaveProductWithText("Hammer");
    }

    @Test
    @Story("Поиск по части названия")
    @Severity(SeverityLevel.NORMAL)
    @Description("Поиск по части названия тоже находит релевантные результаты")
    public void shouldFindProductByPartialName() {
        var result = homePage.search("Hamm");

        result.products().shouldHaveProductWithText("Hammer");
    }

    @Test
    @Story("Поиск несуществующего товара")
    @Severity(SeverityLevel.NORMAL)
    @Description("Поиск по несуществующему запросу возвращает пустой результат и сообщение " +
            "0 products found for 'asdkjaslkdjqwe123456'")
    public void shouldReturnNoResultsForUnknownProduct() {
        var result = homePage.search("asdkjaslkdjqwe123456");

        assertThat(result.products().getErrorMessageText()).
                isEqualTo("0 products found for 'asdkjaslkdjqwe123456'");
    }

    @Test
    @Story("Переход из выдачи в карточку товара")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Клик по товару в результатах поиска открывает его карточку")
    public void shouldOpenProductCardFromSearchResults() {
        var result = homePage.search("Hammer");

        var productPage = result.products().openProduct();

        assertThat(productPage.name()).contains("Hammer");
    }

    @Test
    @Story("Все результаты соответствуют запросу")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Все товары в выдаче содержат поисковый запрос в названии")
    public void shouldShowOnlyMatchingProductsafterSearch() {
        var result = homePage.search("Hammer");

        result.products().shouldAllContain("Hammer");
    }
}