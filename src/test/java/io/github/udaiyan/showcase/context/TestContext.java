package io.github.udaiyan.showcase.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.Scenario;

import java.util.HashMap;
import java.util.Map;

/**
 * Scenario-scoped state, shared between hooks and step classes by PicoContainer
 * (one instance per scenario). Nothing here is static: scenarios are fully
 * isolated and safe to run in parallel.
 */
public class TestContext {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private Scenario currentScenario;

    /** Generic stash for passing state between steps without static fields. */
    private final Map<String, Object> stash = new HashMap<>();

    public Playwright playwright()              { return playwright; }
    public void setPlaywright(Playwright p)     { this.playwright = p; }

    public Browser browser()                    { return browser; }
    public void setBrowser(Browser b)           { this.browser = b; }

    public BrowserContext browserContext()      { return browserContext; }
    public void setBrowserContext(BrowserContext c) { this.browserContext = c; }

    public Page page()                          { return page; }
    public void setPage(Page p)                 { this.page = p; }

    public Map<String, Object> stash()          { return stash; }

    public void setCurrentScenario(Scenario s)  { this.currentScenario = s; }

    public void attachAxeReport(String report) {
        if (currentScenario != null) {
            currentScenario.attach(report.getBytes(), "text/plain", "axe-violations");
        }
    }
}
