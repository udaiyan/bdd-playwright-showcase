package io.github.udaiyan.showcase.hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;

/**
 * Browser lifecycle, owned per scenario.
 *
 * Playwright Java objects are NOT thread-safe: every call must happen on the
 * thread that created the Playwright instance. Cucumber guarantees a scenario's
 * hooks and steps all run on the same worker thread, so creating everything in
 * @Before and disposing in @After is parallel-safe with zero shared state.
 */
public class Hooks {

    private final TestContext context;
    private final FrameworkConfig config;

    public Hooks(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    @Before
    public void startBrowser() {
        Playwright playwright = Playwright.create();
        BrowserType browserType = switch (config.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit"  -> playwright.webkit();
            case "chromium" -> playwright.chromium();
            default -> throw new IllegalArgumentException("Unsupported browser: " + config.browser());
        };

        Browser browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(config.headless()));
        BrowserContext browserContext = browser.newContext();
        browserContext.setDefaultTimeout(config.defaultTimeoutMs());
        Page page = browserContext.newPage();

        context.setPlaywright(playwright);
        context.setBrowser(browser);
        context.setBrowserContext(browserContext);
        context.setPage(page);
    }

    /**
     * @After hooks run highest-order first, so evidence (100) is captured
     * while the browser is still alive, before teardown (0).
     */
    @After(order = 100)
    public void attachEvidenceOnFailure(Scenario scenario) {
        if (scenario.isFailed() && context.page() != null) {
            byte[] screenshot = context.page().screenshot(
                    new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }
    }

    @After(order = 0)
    public void tearDown() {
        // Closing the context closes its pages; each close is null-safe so a
        // failed launch never masks the original test failure.
        if (context.browserContext() != null) context.browserContext().close();
        if (context.browser() != null)        context.browser().close();
        if (context.playwright() != null)     context.playwright().close();
    }
}
