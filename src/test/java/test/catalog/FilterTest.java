package test.catalog;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("practicesoftwaretesting.com")
@Feature("Catalog / Filter")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class FilterTest extends BaseTest {

    @Test()
    @Story("Apply a single filter")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Selecting one filter value narrows the product list in the category")
    public void shouldApplySingleFilter() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Screwdriver");
        catalog.products().shouldHaveFewerThan(totalBefore);
        int totalAfter = catalog.products().count();

        Allure.step("Verify fewer products are show after the filter (" + totalAfter  +
                        " < " + totalBefore + ")",
                () -> assertThat(totalAfter).isLessThan(totalBefore));
        Allure.step("Verify products remain after the filter (" + totalAfter  + ")",
                () -> assertThat(totalAfter).isPositive());
    }

    @Test
    @Story("Apply multiple filters")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Combining several filters narrows the results even further")
    public void shouldApplyMultipleFilters() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Pliers");
        catalog.products().shouldHaveFewerThan(totalBefore);
        int afterOneFilter = catalog.products().count();

        catalog.filters().select("ForgeFlex Tools");
        catalog.products().shouldHaveFewerThan(afterOneFilter);
        int afterTwoFilter = catalog.products().count();

        Allure.step("Verify two filters give fewer products than one (" + afterTwoFilter +
                        " < " + afterOneFilter + ")",
                () -> assertThat(afterTwoFilter).isLessThan(afterOneFilter));
    }

    @Test
    @Story("Reset a filter")
    @Severity(SeverityLevel.NORMAL)
    @Description("Unchecking the filter restores the full product list of the category")
    public void shouldResetFilters() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Hammer");
        catalog.products().shouldHaveFewerThan(totalBefore);
        catalog.filters().select("Hammer");
        catalog.products().shouldHaveCount(totalBefore);
        int afterReset = catalog.products().count();

        Allure.step("Verify the product count is restored after unchecking the filter (" + afterReset +
                        " = " + totalBefore + ")",
                () -> assertThat(afterReset).isEqualTo(totalBefore));
    }

    @Test
    @Story("Results match the filter")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After a filter is applied, every product title in the results matches it")
    public void shouldShowOnlyMatchingProductsAfterFilter() {
        var catalog = homePage.openCategory("Hand Tools");

        catalog.filters().select("Screwdriver");

        catalog.products().shouldAllContain("Screwdriver");
    }

    @DataProvider(name = "categoriesAndFilters")
    public Object[][] categoriesAndFilters() {
        return new Object[][] {
                {"Hand Tools", "Chisels"},
                {"Power Tools", "Drill"},
                {"Other", "Workbench"}
        };
    }

    @Test(dataProvider = "categoriesAndFilters")
    @Story("Filtering across categories")
    @Severity(SeverityLevel.NORMAL)
    @Description("A filter leaves matching products in different categories")
    public void shouldFilterAcrossDifferentCategories(String category, String filterValue) {
        var catalog = homePage.openCategory(category);
        int totalBefore = catalog.products().count();

        catalog.filters().select(filterValue);
        catalog.products().shouldHaveFewerThan(totalBefore);
        int totalAfter = catalog.products().count();

        Allure.step("Verify products remain after the filter (" + totalAfter + ")",
                () -> assertThat(totalAfter).isPositive());
    }
}