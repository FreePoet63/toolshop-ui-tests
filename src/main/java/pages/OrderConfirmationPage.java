package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class OrderConfirmationPage {
    private final SelenideElement successMessage = $("[data-test='payment-success-message']");
    private final SelenideElement placeOrderButton = $("[data-test='finish']");
    private final SelenideElement orderConfirmation = $("#order-confirmation");

    @Step("Verify the payment was successful")
    public OrderConfirmationPage shouldShowSuccess() {
        successMessage.shouldBe(visible).shouldHave(partialText("successful"));
        return this;
    }

    @Step("Verify the payment success message is not displayed")
    public OrderConfirmationPage shouldNotShowSuccess() {
        successMessage.shouldNotBe(visible);
        return this;
    }

    @Step("Place the order")
    public OrderConfirmationPage placeOrder() {
        placeOrderButton.shouldBe(visible).click();
        return this;
    }

    @Step("Verify the invoice number is shown")
    public OrderConfirmationPage shouldShowInvoiceNumber() {
        orderConfirmation.shouldBe(visible).shouldHave(matchText("INV-\\d+"));
        return this;
    }

    @Step("Read the invoice number")
    public String invoiceNumber() {
        String text = orderConfirmation.shouldBe(visible).getText();
        Matcher matcher = Pattern.compile("INV-\\d+").matcher(text);
        if (!matcher.find()) {
            throw new AssertionError("Invoice number not fount in: " + text);
        }
        return matcher.group();
    }
}