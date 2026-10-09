package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.SearchResultPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SearchBar {
    private final SelenideElement input = $("#search-query");
    private final SelenideElement submitButton = $x("//button[@class=\"btn btn-secondary\"]");

    @Step("Ищем товар по запросу «{query}»")
    public SearchResultPage search(String query) {
        input.setValue(query);
        submitButton.click();
        return new SearchResultPage();
    }
}