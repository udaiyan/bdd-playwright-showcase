package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.PopupsPage;
import io.github.udaiyan.showcase.support.DialogManager;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Every expected string here was captured by exploratory testing of the live
 * page (OK and Cancel paths) before automation — assertions encode observed
 * behaviour, not assumptions.
 */
public class PopupsSteps {

    private final TestContext context;
    private final FrameworkConfig config;
    private final DialogManager dialogs;

    public PopupsSteps(TestContext context, FrameworkConfig config, DialogManager dialogs) {
        this.context = context;
        this.config = config;
        this.dialogs = dialogs;
    }

    private PopupsPage popupsPage() {
        return new PopupsPage(context.page());
    }

    @Given("I am on the popups page")
    public void iAmOnThePopupsPage() {
        popupsPage().open(config.baseUrl());
    }

    @When("I open the alert popup")
    public void iOpenTheAlertPopup() {
        dialogs.acceptNext();
        popupsPage().openAlert();
    }

    @When("I accept the confirm popup")
    public void iAcceptTheConfirmPopup() {
        dialogs.acceptNext();
        popupsPage().openConfirm();
    }

    @When("I dismiss the confirm popup")
    public void iDismissTheConfirmPopup() {
        dialogs.dismissNext();
        popupsPage().openConfirm();
    }

    @When("I answer the prompt popup with {string}")
    public void iAnswerThePromptPopupWith(String name) {
        dialogs.acceptNextWith(name);
        popupsPage().openPrompt();
    }

    @When("I dismiss the prompt popup")
    public void iDismissThePromptPopup() {
        dialogs.dismissNext();
        popupsPage().openPrompt();
    }

    @Then("the last dialog message is {string}")
    public void theLastDialogMessageIs(String expected) {
        assertThat(dialogs.lastMessage())
                .as("captured dialog message")
                .isEqualTo(expected);
    }

    @Then("the page shows the result {string}")
    public void thePageShowsTheResult(String text) {
        // Web-first assertion auto-waits for the result written after the
        // dialog closes. Substring match by design (see PopupsPage javadoc).
        assertThat(popupsPage().resultMessage(text)).isVisible();
    }
}
