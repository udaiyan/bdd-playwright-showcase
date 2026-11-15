@tables @regression
Feature: Table lookups and controls
  As a test automation engineer
  I want to look up values in static and interactive tables
  So that data-driven assertions stay readable and row-scoped

  Background:
    Given I am on the tables page

  Scenario Outline: Simple table prices are look-up-able by item
    Then the simple table prices "<item>" at "<price>"

    Examples:
      | item    | price    |
      | Oranges | $3.99    |
      | Laptop  | $1200.00 |
      | Marbles | $1.25    |

  Scenario: Sortable table presents ranked population data with default paging
    Then the sortable table lists "India" at rank "1" with population "1,429"
    And the sortable table shows 10 data rows
    And the sortable table shows the pagination summary "Showing 1 to 10 of 25 entries"

  Scenario: Sorting by country reorders the rows alphabetically
    When I sort the sortable table by "Country"
    Then the sortable table lists "Bangladesh" at rank "8" with population "173"
    And the first data row is for "Bangladesh"

  Scenario: Searching filters the rows
    When I search the sortable table for "india"
    Then the sortable table shows 1 data row
    And the sortable table shows the pagination summary "filtered from 25 total entries"

  Scenario: Changing the page size shows every entry
    When I show "25" entries per page
    Then the sortable table shows 25 data rows
    And the sortable table shows the pagination summary "Showing 1 to 25 of 25 entries"
