package components;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.ProductPage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProductList {
    private final ElementsCollection products = $$(".card");
    private final ElementsCollection titles = $$(".card-title");
    private final SelenideElement errorMessage = $x("//p[@class=\"mb-2\"]");

    @Step("Считаем количество товаров")
    public int count() {
        products.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return products.size();
    }

    @Step("Получаем название всех товаров")
    public List<String> titles() {
        return titles.texts();
    }

    @Step("Получаем название первого товара")
    public String firstTitle() {
        return titles.first().shouldBe(visible).getText();
    }

    @Step("Проверяем, что есть товар с текстом «{text}»")
    public ProductList shouldHaveProductWithText(String text) {
        products.findBy(partialText(text)).shouldBe(visible);
        return this;
    }

    @Step("Ждём, пока товаров станет меньше {before}")
    public ProductList shouldHaveFewerThan(int before) {
        products.shouldHave(CollectionCondition.sizeLessThan(before));
        return this;
    }

    @Step("Ждём, пока товаров станет {expected}")
    public ProductList shouldHaveCount(int expected) {
        products.shouldHave(CollectionCondition.size(expected), Duration.ofSeconds(3));
        return this;
    }

    @Step("Проверяем, что названия всех товаров содержат {text}")
    public ProductList shouldAllContain(String text) {
        titles.shouldHave(CollectionCondition.sizeGreaterThan(0));
        titles.excludeWith(partialText(text)).shouldHave(CollectionCondition.size(0));
        return this;
    }

    @Step("Читаем сообщение об отсутствии товаров")
    public String getErrorMessageText() {
        return errorMessage.shouldBe(visible, Duration.ofSeconds(3)).getText();
    }

    @Step("Открываем карточку выбранного товара")
    public ProductPage openProduct() {
        products.first().hover().click();
        return new ProductPage();
    }

    @Step("Открываем карточку товара «{title}»")
    public ProductPage openProduct(String title) {
        products.findBy(text(title)).shouldBe(visible).click();
        return new ProductPage();
    }
}