package io.github.udaiyan.showcase.steps;

import com.microsoft.playwright.Locator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.TablesPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Expected values were captured by exploratory testing of the live tables
 * (sorting, search, page size) before automation.
 */
public class TablesSteps {

    private final TestContext context;
    private final FrameworkConfig config;

    public TablesSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    private TablesPage tablesPage() {
        return new TablesPage(context.page());
    }

    @Given("I am on the tables page")
    public void iAmOnTheTablesPage() {
        tablesPage().open(config.baseUrl());
    }

    @Then("the simple table prices {string} at {string}")
    public void theSimpleTablePricesAt(String item, String price) {
        Locator row = tablesPage().rowByKey(tablesPage().simpleTable(), item);
        assertThat(tablesPage().cell(row, 1)).hasText(price);
    }

    @Then("the sortable table lists {string} at rank {string} with population {string}")
    public void theSortableTableListsAtRankWithPopulation(String country, String rank, String population) {
        Locator row = tablesPage().rowByKey(tablesPage().sortableTable(), country);
        assertThat(tablesPage().cell(row, 0)).hasText(rank);
        assertThat(tablesPage().cell(row, 2)).hasText(population);
    }

    @Then("the sortable table shows {int} data row(s)")
    public void theSortableTableShowsDataRows(int count) {
        assertThat(tablesPage().dataRows()).hasCount(count);
    }

    @Then("the sortable table shows the pagination summary {string}")
    public void theSortableTableShowsThePaginationSummary(String summary) {
        assertThat(tablesPage().paginationSummary(summary)).isVisible();
    }

    @When("I sort the sortable table by {string}")
    public void iSortTheSortableTableBy(String column) {
        tablesPage().columnHeader(column).click();
    }

    @Then("the first data row is for {string}")
    public void theFirstDataRowIsFor(String country) {
        assertThat(tablesPage().dataRows().first()).containsText(country);
    }

    @When("I search the sortable table for {string}")
    public void iSearchTheSortableTableFor(String term) {
        tablesPage().searchBox().fill(term);
    }

    @When("I show {string} entries per page")
    public void iShowEntriesPerPage(String size) {
        tablesPage().entriesPerPage().selectOption(size);
    }
}
