package pages;

import com.codeborne.selenide.Selenide;
import components.FormAlert;
import components.RegistrationForm;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.TestUser;

public class RegisterPage {
    private final RegistrationForm form = new RegistrationForm();
    private final FormAlert formAlert = new FormAlert();

    @Step("Open the register page")
    public static RegisterPage open() {
        return Selenide.open("/auth/register", RegisterPage.class);
    }

    public RegistrationForm form() {
        return form;
    }

    public RegisterPage submit(TestUser user) {
        Allure.step("Submit the registration form for " + user.email(), () -> {
            form.fill(user).submit();
        });
        return this;
    }

    @Step("Submit the registration form without filling it in")
    public RegisterPage submitEmpty() {
        form.submit();
        return this;
    }

    public LoginPage register(TestUser user) {
        Allure.step("Register the user " + user.email(), () -> {
            submit(user);
        });
        return new LoginPage();
    }

    public RegisterPage shouldShowFormError(String expected) {
        formAlert.shouldHaveText(expected);
        return this;
    }
}