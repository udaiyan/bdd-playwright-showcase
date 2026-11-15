package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

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

    /**
     * Navigate and wait for DOMContentLoaded rather than load.
     *
     * load waits for every resource (images, ads, third-party scripts) to
     * finish; on a real public-sector site that's routinely 8-15s and flaky.
     * DOMContentLoaded fires as soon as the HTML is parsed and the DOM is
     * ready for interaction — which is exactly what a UI test cares about.
     *
     * This also aligns with GDS performance testing standards (First Contentful
     * Paint, Largest Contentful Paint), which all fire before load.
     */
    protected void navigate(String url) {
        page.navigate(url,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }
}
