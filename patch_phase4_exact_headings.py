#!/usr/bin/env python3
"""Phase 4 fix: exact match on data-driven heading/link names inside frames."""
import argparse
from pathlib import Path

TARGET = "src/test/java/io/github/udaiyan/showcase/pages/IframesPage.java"

CONTENT = '''\
package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Iframes page (https://practice-automation.com/iframes/).
 *
 * The page embeds two CROSS-ORIGIN frames (playwright.dev and selenium.dev).
 * Playwright's frameLocator() addresses cross-origin frames natively — no
 * driver-level frame switching and no stale-frame references — which is why
 * GDS teams favour it for One Login-style embedded-auth flows.
 *
 * Frames are located by their accessible name (the title attribute), consistent
 * with the framework's accessibility-first locator policy; the site also
 * provides ids (#iframe-1 / #iframe-2) as an equivalent alternative.
 *
 * Heading/link queries that accept a user-provided name use setExact(true):
 * without it, getByRole does case-insensitive substring matching, so "Downloads"
 * also matched <h2>Other downloads</h2> on the Selenium downloads page. This is
 * the same rule applied to labels in Phase 2 — data-driven role-name queries
 * must be exact.
 *
 * Note: the frames embed live third-party sites, so their inner content can
 * drift. In a production gov project the frame would be stubbed or pointed at
 * a controlled fixture; here we accept the volatility because the practice
 * site is designed around live embeds.
 */
public class IframesPage extends BasePage {

    public IframesPage(Page page) {
        super(page);
    }

    public void open(String baseUrl) {
        navigate(baseUrl + "/iframes/");
    }

    public FrameLocator playwrightFrame() {
        return page.frameLocator("iframe[title='Playwright']");
    }

    public FrameLocator seleniumFrame() {
        return page.frameLocator("iframe[title='Selenium']");
    }

    public Locator playwrightIntroduction() {
        return playwrightFrame().getByText("enables reliable web automation");
    }

    public Locator playwrightDocsLink() {
        return playwrightFrame().getByRole(AriaRole.LINK,
                new FrameLocator.GetByRoleOptions().setName("Docs").setExact(true));
    }

    public Locator playwrightHeading(String name) {
        return playwrightFrame().getByRole(AriaRole.HEADING,
                new FrameLocator.GetByRoleOptions().setName(name).setExact(true));
    }

    public Locator seleniumIntroduction() {
        return seleniumFrame().getByText("Automate any browser, from any language");
    }

    public Locator seleniumDownloadsLink() {
        return seleniumFrame().getByRole(AriaRole.LINK,
                new FrameLocator.GetByRoleOptions().setName("Downloads").setExact(true));
    }

    public Locator seleniumHeading(String name) {
        return seleniumFrame().getByRole(AriaRole.HEADING,
                new FrameLocator.GetByRoleOptions().setName(name).setExact(true));
    }

    public Locator parentText(String text) {
        return page.getByText(text);
    }
}
'''

def main() -> None:
    parser = argparse.ArgumentParser(description="Phase 4 fix: exact heading/link name matching.")
    parser.add_argument("--dir", default=".", help="repo root (default: current directory)")
    args = parser.parse_args()
    path = Path(args.dir) / TARGET
    if not path.exists():
        parser.error(f"{TARGET} not found — run from the repo root")
    path.write_text(CONTENT, encoding="utf-8", newline="\n")
    print(f"Patched {path}")
    print('Re-run: mvn verify "-Dcucumber.filter.tags=@iframes" "-Dheadless=false"')


if __name__ == "__main__":
    main()