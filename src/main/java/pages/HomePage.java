package pages;

import components.CategoryList;
import components.SearchBar;

public class HomePage {
    private final SearchBar searchBar = new SearchBar();
    private final CategoryList categoryList = new CategoryList();

    public SearchResultPage search(String query) {
        return searchBar.search(query);
    }

    public CatalogPage openCategory(String categoryName) {
        categoryList.openCategory(categoryName);
        return new CatalogPage();
    }
}