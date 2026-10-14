package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage {
    @Step("Verify the login page is opened")
    public LoginPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/auth/login"));
        return this;
    }
}