package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Form Fields page (https://practice-automation.com/form-fields/).
 *
 * Locator hierarchy (deliberate, documented):
 *   1. Role/label-first for unique controls — semantic and accessibility-aware.
 *   2. EXACT label matching for data-driven locators: substring matching made
 *      "Red" match the hidden modal's "Name(required)" label ("requi-RED").
 *   3. Test IDs where labels are ambiguous or not properly associated with
 *      their control (hidden Jetpack modal duplicates; the automation select).
 *   4. getByRole excludes hidden elements by default, so the visible Submit
 *      button is unambiguous without a test id.
 */
public class FormFieldsPage extends BasePage {

    // Test data constants — practice site only, never real credentials.
    private static final String TEST_PASSWORD = "Practice-password-1!";
    private static final String TEST_EMAIL = "practice@example.com";

    private final Locator nameInput;
    private final Locator passwordInput;
    private final Locator emailInput;
    private final Locator messageInput;
    private final Locator automationSelect;
    private final Locator submitButton;

    public FormFieldsPage(Page page) {
        super(page);
        // Ambiguous labels (hidden modal duplicates) -> site-provided test IDs.
        nameInput        = page.getByTestId("name-input");
        emailInput       = page.getByTestId("email");
        messageInput     = page.getByTestId("message");
        // Unique, properly associated label -> stay semantic.
        passwordInput    = page.getByLabel("Password");
        // Question text is not a reliably associated label (getByLabel timed
        // out) -> use the site's test instrumentation.
        automationSelect = page.getByTestId("automation");
        submitButton     = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit"));
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/form-fields/");
    }

    public void enterContactDetails(String name, String message) {
        nameInput.fill(name);
        passwordInput.fill(TEST_PASSWORD);
        emailInput.fill(TEST_EMAIL);
        messageInput.fill(message);
    }

    /**
     * Data-driven label locators MUST be exact: getByLabel defaults to
     * case-insensitive substring matching, so "Red" also matched the hidden
     * modal's "Name(required)" label. Exact matching kills that whole class of bug.
     */
    public void chooseDrink(String drink) {
        page.getByLabel(drink, new Page.GetByLabelOptions().setExact(true)).check();
    }

    public void chooseColour(String colour) {
        page.getByLabel(colour, new Page.GetByLabelOptions().setExact(true)).check();
    }

    /** Selects by option value ("yes"/"no"), not visible label ("Yes"/"No"). */
    public void answerAutomation(String value) { automationSelect.selectOption(value); }

    public void submit() { submitButton.click(); }
}
