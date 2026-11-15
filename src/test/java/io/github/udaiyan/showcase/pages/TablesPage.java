package io.github.udaiyan.showcase.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Tables page (https://practice-automation.com/tables/).
 *
 * Two tables, two scoping strategies:
 *  - The sortable/paginated table has a stable id (#tablepress-1) — use it.
 *  - The simple table has NO stable id (a site gap, documented here), so it is
 *    scoped by unique content rather than DOM order — resilient and readable.
 *
 * Row lookups are row-scoped: find the row by a key cell, then read sibling
 * cells by column index. Data-row counts scope to tbody so the column-header
 * row is never counted.
 *
 * Accessibility finding: DataTables' column headers lack proper ARIA roles,
 * so getByRole(COLUMNHEADER) finds nothing. Column clicks use text-based
 * locators scoped to the table header instead; the Phase 6 axe audit
 * corroborates this markup gap.
 */
public class TablesPage extends BasePage {

    public TablesPage(Page page) {
        super(page);
    }

    public void open(String baseUrl) {
        navigate(baseUrl + "/tables/");
    }

    public Locator simpleTable() {
        return page.locator("table")
                .filter(new Locator.FilterOptions().setHasText("Oranges"));
    }

    public Locator sortableTable() {
        return page.locator("#tablepress-1");
    }

    public Locator dataRows() {
        return sortableTable().locator("tbody").getByRole(AriaRole.ROW);
    }

    public Locator rowByKey(Locator table, String key) {
        return table.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText(key));
    }

    public Locator cell(Locator row, int columnIndex) {
        return row.getByRole(AriaRole.CELL).nth(columnIndex);
    }

    /**
     * Column header located by text within the thead, because DataTables'
     * headers lack proper ARIA roles (accessibility finding documented above).
     * Exact match prevents "Country" from matching any future "Other countries"
     * header if the table expands.
     */
    public Locator columnHeader(String name) {
        return sortableTable().locator("thead")
                .getByText(name, new Locator.GetByTextOptions().setExact(true));
    }

    /**
     * DataTables search box label is "Search:" (with colon); exact match
     * prevents collision with the WordPress site search (placeholder "Search")
     * and the search toggler button (label "Search Toggler").
     */
    public Locator searchBox() {
        return page.getByLabel("Search:", new Page.GetByLabelOptions().setExact(true));
    }

    public Locator entriesPerPage() {
        return page.getByLabel("entries per page");
    }

    public Locator paginationSummary(String text) {
        return page.getByText(text);
    }
}
