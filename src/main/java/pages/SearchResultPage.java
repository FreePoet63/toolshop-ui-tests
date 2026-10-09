package pages;

import components.ProductList;
import io.qameta.allure.Step;

public class SearchResultPage {
    private final ProductList productList = new ProductList();

    @Step("Получаем список продуктов")
    public ProductList products() {
        return productList;
    }
}