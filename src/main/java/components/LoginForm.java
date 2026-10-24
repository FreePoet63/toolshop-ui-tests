package components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginForm {
    private final SelenideElement submitButton = $("[data-test='login-submit']");

    public FormField field(Field field) {
        return new FormField(field.id());
    }

    public LoginForm fill(String email, String password) {
        Allure.step("Fill in the login form for " + email, () -> {
            field(Field.EMAIL).type(email);
            field(Field.PASSWORD).type(password);
        });
        return this;
    }

    @Step("Submit the login form")
    public LoginForm submit() {
        submitButton.shouldBe(visible).click();
        return this;
    }
}