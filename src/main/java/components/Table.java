package components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class Table {
    private final ElementsCollection rows = $$("table tbody tr");

    public ElementsCollection rows() {
        return rows;
    }

    public SelenideElement row(String text) {
        return rows.findBy(text(text));
    }
}