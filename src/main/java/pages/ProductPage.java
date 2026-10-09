package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class ProductPage {
    private final SelenideElement productName = $x("//h1[@data-test=\"product-name\"]");

    @Step("Получаем название товара")
    public String name() {
        return productName.getText();
    }
}