package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.HomePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePageSteps {

    private final TestContext context;
    private final FrameworkConfig config;

    public HomePageSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    /**
     * Page objects are created lazily, NOT in the constructor: PicoContainer
     * instantiates glue classes before @Before hooks run, so the Page does not
     * exist yet at construction time. This is a classic Cucumber+DI gotcha.
     */
    private HomePage homePage() {
        return new HomePage(context.page());
    }

    @Given("I open the practice automation home page")
    public void iOpenThePracticeAutomationHomePage() {
        homePage().open(config.baseUrl());
    }

    @Then("the welcome heading is displayed")
    public void theWelcomeHeadingIsDisplayed() {
        // Web-first assertion: auto-waits up to the context default timeout.
        // No Thread.sleep() anywhere in this framework.
        assertThat(homePage().welcomeHeading()).isVisible();
    }
}
