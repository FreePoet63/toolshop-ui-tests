package components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.CartPage;
import pages.LoginPage;
import pages.SearchResultPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class Header {
    private final SelenideElement cartBadge = $("#lblCartCount");
    private final SelenideElement cartLink = $("[data-test='cart-quantity']");
    private final SelenideElement input = $("#search-query");
    private final SelenideElement submitButton = $x("//button[@class=\"btn btn-secondary\"]");
    private final SelenideElement categoryBtn = $x("//button[@data-test=\"nav-categories\"]");
    private final SelenideElement userMenu = $("[data-test='nav-menu']");
    private final SelenideElement signOutLink = $("[data-test='nav-sign-out']");
    private final ElementsCollection categoryMenu = $$x("//ul[@class=\"dropdown-menu show\"]/li");

    @Step("Search for product '{query}'")
    public SearchResultPage search(String query) {
        input.setValue(query);
        submitButton.click();
        return new SearchResultPage();
    }

    @Step("Open category '{categoryName}'")
    public SearchResultPage openCategory(String categoryName) {
        categoryBtn.click();
        categoryMenu.findBy(text(categoryName)).shouldBe(visible).click();
        return new SearchResultPage();
    }

    @Step("Verify that cart badge shows '{expected}'")
    public Header shouldHaveCartCount(String expected) {
        cartBadge.shouldHave(partialText(expected));
        return this;
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartLink.shouldBe(visible).click();
        return new CartPage();
    }

    @Step("Verify the header shows the user '{name}'")
    public Header shouldShowUser(String name) {
        userMenu.shouldHave(partialText(name));
        return this;
    }

    @Step("Sign Out")
    public LoginPage logout() {
        userMenu.shouldBe(visible).click();
        signOutLink.shouldBe(visible).click();
        return new LoginPage();
    }

    @Step("Verify the cart has disappeared from the header")
    public Header shouldNotShowCart() {
        cartLink.shouldBe(disappear);
        return this;
    }
}