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
    @Story("Применение одного фильтра")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Выбор одного значения фильтра сужает список товаров в категории")
    public void shouldApplySingleFilter() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Screwdriver");
        catalog.products().shouldHaveFewerThan(totalBefore);
        int totalAfter = catalog.products().count();

        Allure.step("Проверяем, что после фильтра товаров меньше, чем было (" + totalAfter  +
                        " < " + totalBefore + ")",
                () -> assertThat(totalAfter).isLessThan(totalBefore));
        Allure.step("Проверяем, что после фильтра товары остались (" + totalAfter  + ")",
                () -> assertThat(totalAfter).isPositive());
    }

    @Test
    @Story("Применение нескольких фильтров")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Комбинация нескольких фильтров ещё сильнее сужает выдачу")
    public void shouldApplyMultipleFilters() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Pliers");
        catalog.products().shouldHaveFewerThan(totalBefore);
        int afterOneFilter = catalog.products().count();

        catalog.filters().select("ForgeFlex Tools");
        catalog.products().shouldHaveFewerThan(afterOneFilter);
        int afterTwoFilter = catalog.products().count();

        Allure.step("Проверяем, что с двумя фильтрами товаров меньше, чем с одним (" + afterTwoFilter +
                        " < " + afterOneFilter + ")",
                () -> assertThat(afterTwoFilter).isLessThan(afterOneFilter));
    }

    @Test
    @Story("Сброс фильтра")
    @Severity(SeverityLevel.NORMAL)
    @Description("Cнятие фильтра возвращает полный список товаров категории")
    public void shouldResetFilters() {
        var catalog = homePage.openCategory("Hand Tools");
        int totalBefore = catalog.products().count();

        catalog.filters().select("Hammer");
        catalog.products().shouldHaveFewerThan(totalBefore);
        catalog.filters().select("Hammer");
        catalog.products().shouldHaveCount(totalBefore);
        int afterReset = catalog.products().count();

        Allure.step("Проверяем, что после снятия фильтра товаров столько же, сколько было (" + afterReset +
                        " = " + totalBefore + ")",
                () -> assertThat(afterReset).isEqualTo(totalBefore));
    }

    @Test
    @Story("Проверка результата после фильтрации")
    @Severity(SeverityLevel.CRITICAL)
    @Description("После применения фильтра название каждого товара в выдаче ему соответствует")
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
    @Story("Фильтрация в разных категориях")
    @Severity(SeverityLevel.NORMAL)
    @Description("Фильтр в разных категориях оставляет в выдаче товары")
    public void shouldFilterAcrossDifferentCategories(String category, String filterValue) {
        var catalog = homePage.openCategory(category);
        int totalBefore = catalog.products().count();

        catalog.filters().select(filterValue);
        catalog.products().shouldHaveFewerThan(totalBefore);
        int totalAfter = catalog.products().count();

        Allure.step("Проверяем, что после фильтра товары остались (" + totalAfter + ")",
                () -> assertThat(totalAfter).isPositive());
    }
}