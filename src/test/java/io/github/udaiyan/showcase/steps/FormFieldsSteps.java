package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.FormFieldsPage;
import io.github.udaiyan.showcase.support.DialogManager;

import static org.assertj.core.api.Assertions.assertThat;

public class FormFieldsSteps {

    private final TestContext context;
    private final FrameworkConfig config;
    private final DialogManager dialogs;

    public FormFieldsSteps(TestContext context, FrameworkConfig config, DialogManager dialogs) {
        this.context = context;
        this.config = config;
        this.dialogs = dialogs;
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
        // Expectation declared before the trigger; the shared DialogManager
        // owns capture + acceptance (extracted from this class in Phase 3).
        dialogs.acceptNext();
        formFieldsPage().submit();
    }

    @Then("I see the confirmation {string}")
    public void iSeeTheConfirmation(String expectedMessage) {
        assertThat(dialogs.lastMessage())
                .as("browser dialog message")
                .isEqualTo(expectedMessage);
    }
}
