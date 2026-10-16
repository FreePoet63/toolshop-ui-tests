package pages;

import com.codeborne.selenide.SelenideElement;
import components.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class AccountPage {
    private final SelenideElement title = $("[data-test='page-title']");

    private final Header header = new Header();

    public Header header() {
        return header;
    }

    @Step("Verify the account page is opened")
    public AccountPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/account"));
        title.shouldBe(visible).shouldHave(partialText("My account"));
        return this;
    }
}