package components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public final class Elements {
    private Elements() {}

    public static SelenideElement text(String text) {
        return $(byText(text));
    }
}