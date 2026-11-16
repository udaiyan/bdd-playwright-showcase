@performance @regression
Feature: Performance baseline
  As a test automation engineer
  I want to measure page load times
  So that egregious performance regressions are caught early

  Budgets are generous baselines for a shared third-party site measured over
  the public internet. Each scenario samples three navigations and gates on
  the median to damp network/CDN noise. Precise SLO gating belongs in a
  controlled environment or synthetic monitoring, not here.

  Scenario Outline: Page loads within acceptable threshold
    Given I am on the "<page>" page
    Then the page should load within <threshold> seconds

    Examples:
      | page        | threshold |
      | home        | 5         |
      | form fields | 6         |
      | popups      | 5         |
      | modals      | 6         |
      | iframes     | 6         |
      | tables      | 6         |
