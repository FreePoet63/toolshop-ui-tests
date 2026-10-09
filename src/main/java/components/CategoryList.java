package components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.SearchResultPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CategoryList {
    private final SelenideElement categoryBtn = $x("//button[@data-test=\"nav-categories\"]");
    private final ElementsCollection categoryMenu = $$x("//ul[@class=\"dropdown-menu show\"]/li");

    @Step("Открываем категорию «{categoryName}»")
    public SearchResultPage openCategory(String categoryName) {
        categoryBtn.click();
        categoryMenu.findBy(text(categoryName)).shouldBe(visible).click();
        return new SearchResultPage();
    }
}