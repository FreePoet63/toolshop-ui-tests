package components;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.ProductPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProductList {
    private final ElementsCollection products = $$(".card");
    private final ElementsCollection titles = $$(".card-title");
    private final SelenideElement errorMessage = $x("//p[@class=\"mb-2\"]");

    @Step("Count products")
    public int count() {
        products.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return products.size();
    }

    /* @Step("Получаем название всех товаров")
    public List<String> titles() {
        return titles.texts();
    } */

    @Step("Get the first product title")
    public String firstTitle() {
        return titles.first().shouldBe(visible).getText();
    }

    @Step("Verify a product containing '{text}' is shown")
    public ProductList shouldHaveProductWithText(String text) {
        products.findBy(partialText(text)).shouldBe(visible);
        return this;
    }

    @Step("Wait until fewer than {before} products are shown")
    public ProductList shouldHaveFewerThan(int before) {
        products.shouldHave(CollectionCondition.sizeLessThan(before));
        return this;
    }

    @Step("Wait until exactly {expected} products are shown")
    public ProductList shouldHaveCount(int expected) {
        products.shouldHave(CollectionCondition.size(expected), Duration.ofSeconds(3));
        return this;
    }

    @Step("Verify all product titles contain '{text}'")
    public ProductList shouldAllContain(String text) {
        titles.shouldHave(CollectionCondition.sizeGreaterThan(0));
        titles.excludeWith(partialText(text)).shouldHave(CollectionCondition.size(0));
        return this;
    }

    @Step("Verify the message '{expected}' is shown")
    public ProductList shouldShoeMessage(String expected) {
        errorMessage.shouldHave(exactText(expected));
        return this;
    }

    @Step("Open product '{title}'")
    public ProductPage openProduct(String title) {
        products.findBy(text(title)).shouldBe(visible).click();
        return new ProductPage();
    }

    @Step("Get the title of product #{index}")
    public String titleAt(int index) {
        titles.shouldHave(CollectionCondition.sizeGreaterThan(index));
        return titles.get(index).getText();
    }
}