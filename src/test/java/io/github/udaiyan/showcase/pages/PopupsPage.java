package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Popups page (https://practice-automation.com/popups/).
 *
 * Trigger buttons: exact accessible names (labels observed during recon).
 * Result messages: located by visible text — for transient feedback the
 * message IS the user-facing contract, so we deliberately do not couple to
 * implementation ids. Substring matching also keeps the prompt-cancel
 * assertion independent of the trailing ellipsis character.
 */
public class PopupsPage extends BasePage {

    private final Locator alertButton;
    private final Locator confirmButton;
    private final Locator promptButton;

    public PopupsPage(Page page) {
        super(page);
        alertButton   = button("Alert Popup");
        confirmButton = button("Confirm Popup");
        promptButton  = button("Prompt Popup");
    }

    private Locator button(String name) {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(name).setExact(true));
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/popups/");
    }

    public void openAlert()   { alertButton.click(); }
    public void openConfirm() { confirmButton.click(); }
    public void openPrompt()  { promptButton.click(); }

    public Locator resultMessage(String text) {
        return page.getByText(text);
    }
}
