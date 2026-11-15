#!/usr/bin/env python3
"""
Phase 4: Iframes — adds IframesPage, IframesSteps, iframes.feature.
Run from repo root:  python generate_phase4_iframes.py
Overwrites the files it owns (generator-managed).
"""
import argparse
from pathlib import Path

FILES = {

    "src/test/resources/features/iframes.feature": '''\
@iframes @regression
Feature: Embedded frames
  As a test automation engineer
  I want to interact with content embedded in cross-origin iframes
  So that journeys spanning embedded components (like GOV.UK One Login) are testable

  Background:
    Given I am on the iframes page

  Scenario: Playwright documentation frame loads and navigates in isolation
    Then the Playwright frame shows its introduction
    And the parent page still shows "Me too!" outside the frame
    When I open the docs inside the Playwright frame
    Then the Playwright frame shows the "Installation" page

  Scenario: Selenium documentation frame loads and navigates
    Then the Selenium frame shows its introduction
    When I open the downloads page inside the Selenium frame
    Then the Selenium frame shows the "Downloads" page
''',

    "src/test/java/io/github/udaiyan/showcase/pages/IframesPage.java": '''\
package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Iframes page (https://practice-automation.com/iframes/).
 *
 * The page embeds two CROSS-ORIGIN frames (playwright.dev and selenium.dev).
 * Playwright's frameLocator() addresses cross-origin frames natively — no
 * driver-level frame switching and no stale-frame references — which is why
 * GDS teams favour it for One Login-style embedded-auth flows.
 *
 * Frames are located by their accessible name (the title attribute), consistent
 * with the framework's accessibility-first locator policy; the site also
 * provides ids (#iframe-1 / #iframe-2) as an equivalent alternative.
 *
 * Note: the frames embed live third-party sites, so their inner content can
 * drift. In a production gov project the frame would be stubbed or pointed at
 * a controlled fixture; here we accept the volatility because the practice
 * site is designed around live embeds.
 */
public class IframesPage extends BasePage {

    public IframesPage(Page page) {
        super(page);
    }

    public void open(String baseUrl) {
        navigate(baseUrl + "/iframes/");
    }

    public FrameLocator playwrightFrame() {
        return page.frameLocator("iframe[title='Playwright']");
    }

    public FrameLocator seleniumFrame() {
        return page.frameLocator("iframe[title='Selenium']");
    }

    public Locator playwrightIntroduction() {
        return playwrightFrame().getByText("enables reliable web automation");
    }

    public Locator playwrightDocsLink() {
        return playwrightFrame().getByRole(AriaRole.LINK,
                new FrameLocator.GetByRoleOptions().setName("Docs").setExact(true));
    }

    public Locator playwrightHeading(String name) {
        return playwrightFrame().getByRole(AriaRole.HEADING,
                new FrameLocator.GetByRoleOptions().setName(name));
    }

    public Locator seleniumIntroduction() {
        return seleniumFrame().getByText("Automate any browser, from any language");
    }

    public Locator seleniumDownloadsLink() {
        return seleniumFrame().getByRole(AriaRole.LINK,
                new FrameLocator.GetByRoleOptions().setName("Downloads").setExact(true));
    }

    public Locator seleniumHeading(String name) {
        return seleniumFrame().getByRole(AriaRole.HEADING,
                new FrameLocator.GetByRoleOptions().setName(name));
    }

    /** Parent-document text, to prove frame scope is isolated from the page. */
    public Locator parentText(String text) {
        return page.getByText(text);
    }
}
''',

    "src/test/java/io/github/udaiyan/showcase/steps/IframesSteps.java": '''\
package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.IframesPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Expected strings were captured by exploratory testing of the live frames
 * (in-frame navigation observed in the recon video) before automation.
 */
public class IframesSteps {

    private final TestContext context;
    private final FrameworkConfig config;

    public IframesSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    private IframesPage iframesPage() {
        return new IframesPage(context.page());
    }

    @Given("I am on the iframes page")
    public void iAmOnTheIframesPage() {
        iframesPage().open(config.baseUrl());
    }

    @Then("the Playwright frame shows its introduction")
    public void thePlaywrightFrameShowsItsIntroduction() {
        assertThat(iframesPage().playwrightIntroduction()).isVisible();
    }

    @Then("the parent page still shows {string} outside the frame")
    public void theParentPageStillShowsOutsideTheFrame(String text) {
        // Proves frameLocator scope is isolated: page-level locators still
        // resolve parent-document content while the frame is addressed
        // separately.
        assertThat(iframesPage().parentText(text)).isVisible();
    }

    @When("I open the docs inside the Playwright frame")
    public void iOpenTheDocsInsideThePlaywrightFrame() {
        iframesPage().playwrightDocsLink().click();
    }

    @Then("the Playwright frame shows the {string} page")
    public void thePlaywrightFrameShowsThePage(String name) {
        assertThat(iframesPage().playwrightHeading(name)).isVisible();
    }

    @Then("the Selenium frame shows its introduction")
    public void theSeleniumFrameShowsItsIntroduction() {
        assertThat(iframesPage().seleniumIntroduction()).isVisible();
    }

    @When("I open the downloads page inside the Selenium frame")
    public void iOpenTheDownloadsPageInsideTheSeleniumFrame() {
        iframesPage().seleniumDownloadsLink().click();
    }

    @Then("the Selenium frame shows the {string} page")
    public void theSeleniumFrameShowsThePage(String name) {
        assertThat(iframesPage().seleniumHeading(name)).isVisible();
    }
}
''',
}


def main() -> None:
    parser = argparse.ArgumentParser(description="Phase 4: iframes.")
    parser.add_argument("--dir", default=".", help="repo root (default: current directory)")
    args = parser.parse_args()
    root = Path(args.dir)
    if not (root / "pom.xml").exists():
        parser.error(f"No pom.xml in {root.resolve()} — run from the repo root")

    for rel_path, content in FILES.items():
        path = root / rel_path
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8", newline="\n")
        print(f"  wrote  {rel_path}")

    print("\\nNext:")
    print('  mvn verify "-Dcucumber.filter.tags=@iframes" "-Dheadless=false"   # 2 scenarios')
    print('  mvn verify                                                       # full suite: 18 scenarios')


if __name__ == "__main__":
    main()