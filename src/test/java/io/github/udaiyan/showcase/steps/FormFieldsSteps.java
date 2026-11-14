package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.FormFieldsPage;

import static org.assertj.core.api.Assertions.assertThat;

public class FormFieldsSteps {

    /** Stash key: message text of the last browser dialog seen in this scenario. */
    private static final String LAST_DIALOG_MESSAGE = "lastDialogMessage";

    private final TestContext context;
    private final FrameworkConfig config;

    public FormFieldsSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    private FormFieldsPage formFieldsPage() {
        return new FormFieldsPage(context.page());
    }

    @Given("I am on the form fields page")
    public void iAmOnTheFormFieldsPage() {
        formFieldsPage().open(config.baseUrl());
    }

    @When("I complete the form with name {string} and message {string}")
    public void iCompleteTheForm(String name, String message) {
        formFieldsPage().enterContactDetails(name, message);
    }

    @When("I choose {string} as my drink and {string} as my colour")
    public void iChooseDrinkAndColour(String drink, String colour) {
        formFieldsPage().chooseDrink(drink);
        formFieldsPage().chooseColour(colour);
    }

    @When("I answer {string} to liking automation")
    public void iAnswerToLikingAutomation(String answer) {
        formFieldsPage().answerAutomation(answer);
    }

    @When("I submit the form")
    public void iSubmitTheForm() {
        // Register the dialog handler BEFORE the action that triggers it.
        // With no listener, Playwright auto-dismisses dialogs and the assertion
        // would have nothing to read. The handler runs on this scenario's thread
        // while click() is in flight, so the stash is populated by the time
        // submit() returns. The stash (not a page field) holds the evidence
        // because page objects are created lazily per call.
        context.page().onDialog(dialog -> {
            context.stash().put(LAST_DIALOG_MESSAGE, dialog.message());
            dialog.accept();
        });
        formFieldsPage().submit();
    }

    @Then("I see the confirmation {string}")
    public void iSeeTheConfirmation(String expectedMessage) {
        // The dialog is gone; its captured text is the evidence.
        assertThat(context.stash())
                .as("browser dialog message")
                .containsEntry(LAST_DIALOG_MESSAGE, expectedMessage);
    }
}
