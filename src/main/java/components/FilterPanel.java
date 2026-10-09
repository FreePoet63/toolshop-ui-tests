package components;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;

public class FilterPanel {
    private final ElementsCollection filterOptions = $$x("//label[input[@class=\"icheck\"]]");

    @Step("Выбираем фильтр «{value}»")
    public FilterPanel select(String value) {
        filterOptions.findBy(text(value)).shouldBe(interactable).click();
        return this;
    }
}