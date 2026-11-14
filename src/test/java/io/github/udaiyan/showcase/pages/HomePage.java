package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage extends BasePage {

    private final Locator welcomeHeading;

    public HomePage(Page page) {
        super(page);
        // getByRole name matching is substring + case-insensitive by default.
        this.welcomeHeading = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("Welcome to your software automation practice website"));
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/");
    }

    public Locator welcomeHeading() {
        return welcomeHeading;
    }
}
