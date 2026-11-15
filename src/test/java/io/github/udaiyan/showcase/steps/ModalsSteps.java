package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.ModalsPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Expected strings and the submission-confirmation signal were captured by
 * exploratory testing of the live modals before automation. The valid-submit
 * assertion keys off the "contact-form-sent" URL parameter, which only appears
 * after the server accepts the POST.
 */
public class ModalsSteps {

    private final TestContext context;
    private final FrameworkConfig config;

    public ModalsSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    private ModalsPage modalsPage() {
        return new ModalsPage(context.page());
    }

    @Given("I am on the modals page")
    public void iAmOnTheModalsPage() {
        modalsPage().open(config.baseUrl());
    }

    @When("I open the simple modal")
    public void iOpenTheSimpleModal() {
        modalsPage().openSimpleModal();
    }

    @When("I open the form modal")
    public void iOpenTheFormModal() {
        modalsPage().openFormModal();
    }

    @Then("the simple modal shows the title {string}")
    public void theSimpleModalShowsTheTitle(String expected) {
        assertThat(modalsPage().simpleModalTitle()).hasText(expected);
    }

    @Then("the simple modal shows the message {string}")
    public void theSimpleModalShowsTheMessage(String expected) {
        // Treat apostrophes as wildcards: WordPress may render curly quotes.
        assertThat(modalsPage().simpleModalBody())
                .containsText(Pattern.compile(expected.replace("'", ".")));
    }

    @When("I close the visible modal")
    public void iCloseTheVisibleModal() {
        modalsPage().closeVisibleModal();
    }

    @Then("the simple modal is no longer visible")
    public void theSimpleModalIsNoLongerVisible() {
        assertThat(modalsPage().simpleModalTitle()).isHidden();
    }

    @Then("the form modal is visible")
    public void theFormModalIsVisible() {
        assertThat(modalsPage().formModalTitle()).isVisible();
    }

    @Then("the form modal shows a name, email and message field")
    public void theFormModalShowsANameEmailAndMessageField() {
        assertThat(modalsPage().nameField()).isVisible();
        assertThat(modalsPage().emailField()).isVisible();
        assertThat(modalsPage().messageField()).isVisible();
    }

    @When("I submit the form modal without entering a name")
    public void iSubmitTheFormModalWithoutEnteringAName() {
        modalsPage().submit();
    }

    @Then("the form modal shows the error {string}")
    public void theFormModalShowsTheError(String expected) {
        assertThat(modalsPage().errorByText(expected)).isVisible();
    }

    @When("I complete the form modal with name {string}, email {string} and message {string}")
    public void iCompleteTheFormModal(String name, String email, String message) {
        modalsPage().fillContactForm(name, email, message);
    }

    @When("I submit the form modal")
    public void iSubmitTheFormModal() {
        modalsPage().submit();
    }

    @Then("the submission is confirmed")
    public void theSubmissionIsConfirmed() {
        // Definitive proof of server acceptance: the redirect query parameter.
        assertThat(context.page()).hasURL(Pattern.compile("contact-form-sent="));
        assertThat(modalsPage().confirmationMessage()).isVisible();
    }
}
