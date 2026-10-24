package pages;

import com.codeborne.selenide.SelenideElement;
import components.BillingAddressForm;
import components.GuestForm;
import components.PaymentForm;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.PaymentData;
import model.TestUser;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CheckoutPage {
    private final SelenideElement proceedButton = $("[data-test='proceed-2']");
    private final SelenideElement guestProceedButton = $("[data-test='proceed-2-guest']");
    private final SelenideElement loginSubmit = $("[data-test='login-submit']");

    private final GuestForm guest = new GuestForm();
    private final BillingAddressForm billing = new BillingAddressForm();
    private final PaymentForm payment = new PaymentForm();

    public GuestForm guest() {
        return guest;
    }

    public BillingAddressForm billing() {
        return billing;
    }

    public PaymentForm payment() {
        return payment;
    }

    @Step("Verify the checkout page is opened")
    public CheckoutPage shouldBeOpen() {
        webdriver().shouldHave(urlContaining("/checkout"));
        return this;
    }

    @Step("Verify the user is offered to sign in or to continue as a guest")
    public CheckoutPage shouldOfferSignInOrGuestCheckout() {
        loginSubmit.shouldBe(visible);
        guest.shouldBeOffered();
        return this;
    }

    @Step("Continue as the signed in user")
    public CheckoutPage continueAsSignedIn() {
        proceedButton.shouldBe(visible).click();
        return this;
    }

    public CheckoutPage continueAsGuest(TestUser guestDetails) {
        Allure.step("Continue as a guest " + guestDetails.email(), () -> {
            guest.open().fill(guestDetails).submit();
            guestProceedButton.shouldBe(visible).click();
        });
        return this;
    }

    @Step("Continue with the prefilled billing address")
    public CheckoutPage continueWithPrefilledAddress() {
        billing.submit();
        return this;
    }

    public CheckoutPage fillBilling(TestUser user) {
        Allure.step("Fill in the billing address and continue", () -> {
            billing.fill(user).submit();
        });
        return this;
    }

    public OrderConfirmationPage pay(PaymentData data) {
        Allure.step("Pay with " + data.method().label(), () -> {
            payment.fill(data).confirm();
        });
        return new OrderConfirmationPage();
    }
}