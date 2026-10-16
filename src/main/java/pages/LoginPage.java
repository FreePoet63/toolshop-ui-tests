package pages;

import com.codeborne.selenide.Selenide;
import components.FormAlert;
import components.LoginForm;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.TestUser;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage {
    private final LoginForm form = new LoginForm();
    private final FormAlert formAlert = new FormAlert();

    @Step("Open the login page")
    public static LoginPage open() {
        return Selenide.open("/auth/login", LoginPage.class);
    }

    public LoginForm form() {
        return form;
    }

    @Step("Verify the login page is opened")
    public LoginPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/auth/login"));
        return this;
    }

    public LoginPage submit(String email, String password) {
        Allure.step("Submit the login form with email " + email, () -> {
            form.fill(email, password).submit();
        });
        return this;
    }

    public AccountPage loginAs(TestUser user) {
        Allure.step("Log in as " + user.email(), () -> {
            submit(user.email(), user.password());
        });
        return new AccountPage();
    }

    @Step("Submit the login form without filling it in")
    public LoginPage submitEmpty() {
        form.submit();
        return this;
    }

    public LoginPage shouldShowFormError(String expected) {
        formAlert.shouldHaveText(expected);
        return this;
    }
}