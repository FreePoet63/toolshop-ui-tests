package test.search;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("practicesoftwaretesting.com")
@Feature("Search")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class SearchTest extends BaseTest {

    @Test
    @Story("Search for an existing product")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Searching by a full product name returns that product")
    public void shouldFindExistingProduct() {
        var result = homePage.search("Hammer");

        result.products().shouldHaveProductWithText("Hammer");
    }

    @Test
    @Story("Search by part of a name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Searching by part of a product name also returns relevant results")
    public void shouldFindProductByPartialName() {
        var result = homePage.search("Hamm");

        result.products().shouldHaveProductWithText("Hammer");
    }

    @Test
    @Story("Search for a non-existent product")
    @Severity(SeverityLevel.NORMAL)
    @Description("Searching for a non-existent product shows no results and the message " +
            "\"0 products found for 'asdkjaslkdjqwe123456'\"")
    public void shouldReturnNoResultsForUnknownProduct() {
        var result = homePage.search("asdkjaslkdjqwe123456");

        result.products()
                .shouldHaveCount(0)
                .shouldShoeMessage("0 products found for 'asdkjaslkdjqwe123456'");
    }

    @Test
    @Story("Open a product from search results")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking a product in the search results opens its page")
    public void shouldOpenProductCardFromSearchResults() {
        var result = homePage.search("Hammer");

        var productPage = result.products().openProduct("Hammer");

        Allure.step("Verify the expected product page is opened",
                () -> assertThat(productPage.name()).contains("Hammer"));
    }

    @Test
    @Story("All results match the query")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Every product title in the result contains the search query")
    public void shouldShowOnlyMatchingProductsAfterSearch() {
        var result = homePage.search("Hammer");

        result.products().shouldAllContain("Hammer");
    }
}