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
 * In-frame clicks go through IframesPage.clickInFrame(), which scrolls and
 * uses a longer actionability timeout for cross-origin SPA resilience.
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
        assertThat(iframesPage().parentText(text)).isVisible();
    }

    @When("I open the docs inside the Playwright frame")
    public void iOpenTheDocsInsideThePlaywrightFrame() {
        iframesPage().clickInFrame(iframesPage().playwrightDocsLink());
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
        iframesPage().clickInFrame(iframesPage().seleniumDownloadsLink());
    }

    @Then("the Selenium frame shows the {string} page")
    public void theSeleniumFrameShowsThePage(String name) {
        assertThat(iframesPage().seleniumHeading(name)).isVisible();
    }
}
