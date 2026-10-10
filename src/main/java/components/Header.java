package components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.SearchResultPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class Header {
    private final SelenideElement cartBadge = $("#lblCartCount");
    private final SelenideElement input = $("#search-query");
    private final SelenideElement submitButton = $x("//button[@class=\"btn btn-secondary\"]");
    private final SelenideElement categoryBtn = $x("//button[@data-test=\"nav-categories\"]");
    private final ElementsCollection categoryMenu = $$x("//ul[@class=\"dropdown-menu show\"]/li");

    @Step("Ищем товар по запросу «{query}»")
    public SearchResultPage search(String query) {
        input.setValue(query);
        submitButton.click();
        return new SearchResultPage();
    }

    @Step("Открываем категорию «{categoryName}»")
    public SearchResultPage openCategory(String categoryName) {
        categoryBtn.click();
        categoryMenu.findBy(text(categoryName)).shouldBe(visible).click();
        return new SearchResultPage();
    }

    @Step("Проверяем, что в значке корзины {expected}")
    public Header shouldHaveCartCount(String expected) {
        cartBadge.shouldHave(partialText(expected));
        return this;
    }
}