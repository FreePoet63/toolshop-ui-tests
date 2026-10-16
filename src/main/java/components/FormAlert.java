package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FormAlert {
    private final SelenideElement alert = $(".help-block");

    public FormAlert shouldHaveText(String expected) {
        Allure.step("Verify the form shows the error '" + expected + "'", () -> {
            alert.shouldBe(visible).shouldHave(partialText(expected));
        });
        return this;
    }
}