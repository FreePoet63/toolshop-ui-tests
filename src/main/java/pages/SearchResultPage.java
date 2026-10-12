package pages;

import components.ProductList;
import io.qameta.allure.Step;

public class SearchResultPage {
    private final ProductList productList = new ProductList();

    @Step("Get the product list")
    public ProductList products() {
        return productList;
    }
}