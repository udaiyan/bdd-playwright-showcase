package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Page;

/**
 * Pages expose navigation, actions and locators — assertions live in steps.
 * Locators are role/label-first (getByRole/getByLabel) wherever possible:
 * resilient, and they double as an implicit accessibility check.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }
}
