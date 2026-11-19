package io.github.udaiyan.showcase.support;

import com.microsoft.playwright.Dialog;
import io.github.udaiyan.showcase.context.TestContext;

import java.util.function.Consumer;

/**
 * Scenario-scoped JavaScript dialog handling, shared by step classes via
 * PicoContainer.
 *
 * Declare the expectation BEFORE the action that triggers the dialog:
 *   dialogs.acceptNext();          // alert / confirm OK
 *   dialogs.dismissNext();         // confirm / prompt Cancel
 *   dialogs.acceptNextWith("Ada"); // prompt OK with input
 *
 * Registration is lazy because glue classes are constructed before @Before
 * hooks create the Page. One listener per scenario; the action stays swappable.
 * With no listener Playwright auto-dismisses dialogs, so an unexpected dialog
 * never hangs the run — the message assertion will expose it instead.
 */
public class DialogManager {

    private final TestContext context;
    private Consumer<Dialog> nextAction = Dialog::accept;
    private String lastMessage;
    private boolean listening;

    public DialogManager(TestContext context) {
        this.context = context;
    }

    public void acceptNext() {
        nextAction = Dialog::accept;
        listen();
    }

    public void dismissNext() {
        nextAction = Dialog::dismiss;
        listen();
    }

    public void acceptNextWith(String input) {
        nextAction = dialog -> dialog.accept(input);
        listen();
    }

    /** Message of the most recent dialog in this scenario (null if none seen). */
    public String lastMessage() {
        return lastMessage;
    }

    private void listen() {
        if (listening) {
            return;
        }
        listening = true;
        context.page().onDialog(dialog -> {
            lastMessage = dialog.message();
            nextAction.accept(dialog);
        });
    }
}
