# bdd-playwright-showcase

GDS-aligned BDD test framework: **Java 21 · Cucumber 7 · Playwright · axe-core (coming)**.
Tests run against [practice-automation.com](https://practice-automation.com).

## Run

    mvn verify                          # headless Chromium
    mvn verify -Dheadless=false         # watch it run
    mvn verify -Dbrowser=firefox        # cross-browser
    mvn verify -Dcucumber.filter.tags=@smoke

HTML report: `target/cucumber-reports/cucumber.html`
