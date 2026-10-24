package components;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class FilterPanel {
    private final ElementsCollection filterOptions = $$("label:has(input.icheck)");

    @Step("Select filter '{value}'")
    public FilterPanel select(String value) {
        filterOptions.findBy(text(value)).shouldBe(interactable).click();
        return this;
    }
}