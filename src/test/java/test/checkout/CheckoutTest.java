package test.checkout;

import api.UserApi;
import base.BaseTest;
import components.Header;
import io.qameta.allure.*;
import model.PaymentData;
import model.PaymentMethod;
import model.TestUser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

import static com.codeborne.selenide.Selenide.open;

@Epic("practicesoftwaretesting.com")
@Feature("Checkout")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class CheckoutTest extends BaseTest {
    private static final String EMPTY_FORM_ERROR = "Unknown error";
    private static final String EMAIL = "guest@shop.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    @Step("Create a user through the API ang sign in")
    private TestUser signedInUser() {
        var user = UserApi.registerRandom();
        LoginPage.open().loginAs(user).shouldBeOpened();
        return user;
    }

    @Step("Add product #{index} from the {categoryName} category to the cart")
    private String addToCart(String categoryName, int index) {
        open("/");
        var catalog = homePage.openCategory(categoryName);
        String title = catalog.products().titleAt(index);
        ProductPage productPage = catalog.products().openProduct(title);
        productPage.addToCart().toast().shouldShowAdded();
        return title;
    }

    @Step("Go through the checkout to the payment step")
    private CheckoutPage openPaymentStep() {
        signedInUser();
        addToCart("Hand Tools", 0);
        return new Header().openCart()
                .proceedToCheckout()
                .continueAsSignedIn()
                .continueWithPrefilledAddress();
    }

    @DataProvider(name = "paymentMethods")
    public Object[][] paymentMethods() {
        return new Object[][] {
                {PaymentData.creditCard()},
                {PaymentData.bankTransfer()},
                {PaymentData.cashOnDelivery()},
                {PaymentData.giftCard()}
        };
    }

    @Test(dataProvider = "paymentMethods")
    @Story("Place an order")
    @Severity(SeverityLevel.BLOCKER)
    @Description("A signed in user places an order with each payment method using the prefilled address")
    public void shouldPlaceOrder(PaymentData payment) {
        openPaymentStep()
                .pay(payment)
                .shouldShowSuccess()
                .placeOrder()
                .shouldShowInvoiceNumber();
    }

    @Test
    @Story("Place an order")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After the order is placed the invoice number is shown and the cart disappears")
    public void shouldRemoveCartAfterOrderIsPlaced() throws InterruptedException {
        openPaymentStep()
                .pay(PaymentData.creditCard())
                .shouldShowSuccess()
                .placeOrder()
                .shouldShowInvoiceNumber();

        new Header().shouldNotShowCart();
    }

    @Test
    @Story("Guest checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("A guest sees the sign in form and the option to continue as a guest")
    public void shouldOfferGuestToSignInOrContinueAsGuest() {
        addToCart("Hand Tools", 0);

        new Header().openCart()
                .proceedToCheckout()
                .shouldOfferSignInOrGuestCheckout();
    }

    @Test
    @Story("Guest checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("A guest places an order by entering contact details and the billing address")
    public void shouldPlaceOrderSuccessfullyAsGuest() {
        var guest = TestUser.random();
        addToCart("Hand Tools", 0);

        new Header().openCart()
                .proceedToCheckout()
                .continueAsGuest(guest)
                .fillBilling(guest)
                .pay(PaymentData.cashOnDelivery())
                .shouldShowSuccess()
                .placeOrder()
                .shouldShowInvoiceNumber();
    }

    @Test
    @Story("Order history")
    @Severity(SeverityLevel.CRITICAL)
    @Description("A placed order appears in the order history with the cart total")
    public void shouldShowOrderInHistory() {
        signedInUser();
        addToCart("Hand Tools", 0);
        var cart = new Header().openCart();
        var total = cart.total();

        var confirmation = cart.proceedToCheckout()
                .continueAsSignedIn()
                .continueWithPrefilledAddress()
                .pay(PaymentData.creditCard());
        confirmation.shouldShowSuccess().placeOrder().shouldShowInvoiceNumber();
        String invoice = confirmation.invoiceNumber();

        InvoicesPage.open()
                .shouldContainInvoice(invoice)
                .shouldHaveTotal(invoice, total);
    }

    @Test
    @Story("Billing address autofill")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The billing address is prefilled from the profile saved at registration")
    public void shouldAutofillBillingAddressFromProfile() {
        var user = signedInUser();
        addToCart("Hand Tools", 0);

        var checkout = new Header().openCart()
                .proceedToCheckout()
                .continueAsSignedIn();

        checkout.billing().shouldBePrefilledFrom(user);
    }

    @Test()
    @Story("Billing address")
    @Severity(SeverityLevel.NORMAL)
    @Description("A user can place an order with a number house that differs from the profile")
    public void shouldPlaceOrderWithNewBillingAddress() {
        var user = signedInUser();
        addToCart("Hand Tools", 0);

        new Header().openCart()
                .proceedToCheckout()
                .continueAsSignedIn()
                .fillBilling(user.withHouseNumber("99"))
                .pay(PaymentData.creditCard())
                .shouldShowSuccess()
                .placeOrder()
                .shouldShowInvoiceNumber();
    }

    @DataProvider(name = "invalidPaymentFields")
    public Object[][] invalidPaymentFields() {
        return new Object[][]{
                {PaymentData.creditCard(), PaymentData.CARD_NUMBER, ",",
                        "Invalid card number format."},
                {PaymentData.creditCard(), PaymentData.CARD_EXPIRATION, ",",
                        "Invalid date format. Use MM/YYYY."},
                {PaymentData.creditCard(), PaymentData.CARD_CVV, ",",
                        "CVV must be 3 or 4 digits."},
                {PaymentData.bankTransfer(), PaymentData.BANK_NAME, ",",
                        "Bank name can only contain letters and spaces."},
                {PaymentData.bankTransfer(), PaymentData.ACCOUNT_NAME, ",",
                        "Account name can contain letters, numbers, spaces, periods, apostrophes, and hyphens."},
                {PaymentData.bankTransfer(), PaymentData.ACCOUNT_NUMBER, ",",
                        "Account number must be numeric."},
                {PaymentData.giftCard(), PaymentData.GIFT_CARD_NUMBER, ",",
                        "Please enter a valid gift card number: exactly 16 letters and/or digits."},
                {PaymentData.giftCard(), PaymentData.VALIDATION_CODE, ",",
                        "Please enter a valid validation code: exactly 4 letters and/or digits."}
        };
    }

    @Test(dataProvider = "invalidPaymentFields")
    @Story("Payment validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("An invalid value in one payment field shows the validation message")
    public void shouldShowErrorForInvalidPaymentValue(PaymentData payment, String fieldId,
                                                      String invalidValue, String expectedError) {
        var checkout = openPaymentStep();

        checkout.payment().fill(payment);
        checkout.payment().field(fieldId).type(invalidValue);

        checkout.payment().shouldShowError(expectedError);
    }

    @Test
    @Story("Payment validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirming a credit card payment with empty fields shows the generic error and places no order")
    public void shouldShowGeneralErrorForEmptyCardForm() {
        var checkout = openPaymentStep();

        checkout.payment().selectMethod(PaymentMethod.CREDIT_CARD).confirm();

        checkout.payment().shouldShowError(EMPTY_FORM_ERROR);
        new OrderConfirmationPage().shouldNotShowSuccess();
    }
}