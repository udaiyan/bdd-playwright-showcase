package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

/**
 * Modals page (https://practice-automation.com/modals/).
 *
 * Accessibility finding: the modal titles are styled text, NOT semantic
 * headings (no h1-h6, no role="heading"), so role-based heading queries find
 * nothing. Titles are therefore located by their observed text. This defect is
 * documented rather than hidden; the Phase 4 axe audit corroborates it.
 *
 * The form modal performs a real server-side POST; a successful submission
 * redirects with a "contact-form-sent" query parameter (see ModalsSteps).
 */
public class ModalsPage extends BasePage {

    public ModalsPage(Page page) {
        super(page);
    }

    public void open(String baseUrl) {
        navigate(baseUrl + "/modals/");
    }

    public void openSimpleModal() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Simple Modal").setExact(true)).click();
    }

    public void openFormModal() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Form Modal").setExact(true)).click();
    }

    public Locator simpleModalTitle() {
        return page.getByText("Simple Modal", new Page.GetByTextOptions().setExact(true))
                .and(page.locator(":not(button)"));
    }

    public Locator simpleModalBody() {
        return page.getByText(Pattern.compile("Hi, I.m a simple modal\\."));
    }

    public Locator formModalTitle() {
        return page.getByText("Modal Containing A Form", new Page.GetByTextOptions().setExact(true));
    }

    public Locator nameField()    { return page.getByLabel("Name(required)"); }
    public Locator emailField()   { return page.getByLabel("Email"); }
    public Locator messageField() { return page.getByLabel("Message"); }

    public Locator confirmationMessage() {
        return page.getByText("Thank you for your response.");
    }

    public Locator errorByText(String text) {
        return page.getByText(text);
    }

    public void fillContactForm(String name, String email, String message) {
        nameField().fill(name);
        emailField().fill(email);
        messageField().fill(message);
    }

    public void submit() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit").setExact(true)).click();
    }

    public void closeVisibleModal() {
        page.locator("button:visible")
                .filter(new Locator.FilterOptions().setHasText("\u00d7"))
                .click();
    }
}
