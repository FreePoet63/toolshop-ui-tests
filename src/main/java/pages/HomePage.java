package pages;

import components.Header;

public class HomePage {
    private final Header header = new Header();

    public SearchResultPage search(String query) {
        return header.search(query);
    }

    public CatalogPage openCategory(String categoryName) {
        header.openCategory(categoryName);
        return new CatalogPage();
    }
}