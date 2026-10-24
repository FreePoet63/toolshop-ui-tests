package components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class FormField {
    private static final String INPUT = "[data-test='%s']";
    private static final String ERROR = "[data-test='%s-error']";

    private final String name;
    private final SelenideElement input;
    private final SelenideElement error;

    public FormField(String name) {
        this.name = name;
        this.input = $(String.format(INPUT, name));
        this.error = $(String.format(ERROR, name));
    }

    public FormField type(String value) {
        String shown = name.contains("password") ? "*****" : value;
        Allure.step("Enter '" + shown + "' into the '" + name + "' field", () -> {
            input.shouldBe(visible).setValue(value);
        });
        return this;
    }

    public FormField setValueByScript(String value) {
        Allure.step("Set '" + value + "' in the '" + name + "' field", () -> {
            input.shouldBe(visible).click();
            executeJavaScript(
                    "arguments[0].value = arguments[1];"
                            + "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                            + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
                    input, value);
            input.sendKeys(Keys.SPACE, Keys.BACK_SPACE);
            input.pressTab();
            input.shouldHave(Condition.value(value));
        });
        return this;
    }

    public FormField shouldHaveValue(String expected) {
        Allure.step("Verify the '" + name + "' field contains '" + expected + "'", () -> {
            input.shouldHave(value(expected));
        });
        return this;
    }

    public FormField shouldShowError() {
        Allure.step("Verify the '" + name + "' field show a validation error", () -> {
            error.shouldBe(visible);
        });
        return this;
    }

    public FormField shouldHaveError(String expected) {
        Allure.step("Verify the '" + name + "' field show the error '" + expected + "'", () -> {
            error.shouldHave(partialText(expected));
        });
        return this;
    }

    public FormField shouldHaveNoError() {
        Allure.step("Verify the '" + name + "' field show no error", () -> {
            error.shouldNotBe(visible);
        });
        return this;
    }
    public FormField select(String text) {
        Allure.step("Select '" + text + "' in the '" + name + "' field", () -> {
            input.shouldBe(visible).selectOption(text);
        });
        return this;
    }
}