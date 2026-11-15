# bdd-playwright-showcase

GDS-aligned BDD test framework: **Java 21 · Cucumber 7 · Playwright · axe-core accessibility**.
Tests run against [practice-automation.com](https://practice-automation.com).

## Run

    mvn verify                          # headless Chromium
    mvn verify -Dheadless=false         # watch it run
    mvn verify -Dbrowser=firefox        # cross-browser
    mvn verify -Dcucumber.filter.tags=@smoke

HTML report: `target/cucumber-reports/cucumber.html`

## Accessibility testing

The framework integrates **axe-core** for automated WCAG 2.2 AA compliance checks.
Run the accessibility suite:

    mvn verify -Dcucumber.filter.tags=@accessibility

**Important**: automated accessibility testing catches only **~30% of issues** (per GDS manual).
This suite complements manual testing with assistive technologies (screen readers, keyboard navigation).

### Known issues

The practice site embeds third-party content (ads, cross-origin iframes, DataTables plugin)
which axe reports as violations. These are documented as `@known_issues` scenarios and
excluded from the fail criteria. In a production gov project these would be:
- Stubbed or pointed at controlled fixtures
- Audited separately with documented baselines
- Published in the accessibility statement

## Phases

1. **Walking skeleton** — framework skeleton, smoke test
2. **Form fields** — data-driven scenarios, documented locator hierarchy
3. **Popups & modals** — dialog handling, server-side submission assertions
4. **Iframes** — cross-origin frameLocator, JS click fallback for third-party SPAs
5. **Tables** — row-scoped lookups, sort/search/pagination
6. **Accessibility** — axe-core WCAG 2.2 AA scans with known-issue baselines
