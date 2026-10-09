package pages;

import components.FilterPanel;
import components.ProductList;
import io.qameta.allure.Step;

public class CatalogPage {
    private final FilterPanel filterPanel = new FilterPanel();
    private final ProductList productList = new ProductList();

    public FilterPanel filters() {
        return filterPanel;
    }

    @Step("Получаем список продуктов")
    public ProductList products() {
        return productList;
    }
}