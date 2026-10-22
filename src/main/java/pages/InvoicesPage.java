package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Money;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class InvoicesPage {
    private final ElementsCollection rows = $$("table tbody tr");

    @Step("Open the order history")
    public static InvoicesPage open() {
        return Selenide.open("/account/invoices", InvoicesPage.class);
    }

    @Step("Verify the order history contains the invoice '{number}'")
    public InvoicesPage shouldContainInvoice(String number) {
        rows.findBy(text(number)).shouldBe(visible);
        return this;
    }

    public InvoicesPage shouldHaveTotal(String number, BigDecimal expected) {
        Allure.step("Verify the invoice '" + number + "' has the total " + Money.format(expected), () -> {
            rows.findBy(text(number)).shouldHave(partialText(Money.plain(expected)));
        });
        return this;
    }
}