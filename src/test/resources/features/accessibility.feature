@accessibility @regression
Feature: Accessibility compliance (WCAG 2.2 AA)
  As a test automation engineer
  I want to run automated accessibility scans against each page
  So that critical and serious violations are caught early

  Note: automated accessibility testing catches only ~30% of issues (GDS manual).
  This suite complements manual testing with assistive technologies.

  @known_issues
  Scenario: Home page meets WCAG 2.2 AA (excluding known third-party violations)
    Given I am on the home page
    Then the page has no critical or serious accessibility violations excluding known issues

  @known_issues
  Scenario: Form fields page meets WCAG 2.2 AA (excluding known third-party violations)
    Given I am on the form fields page
    Then the page has no critical or serious accessibility violations excluding known issues

  @known_issues
  Scenario: Popups page meets WCAG 2.2 AA (excluding known third-party violations)
    Given I am on the popups page
    Then the page has no critical or serious accessibility violations excluding known issues

  @known_issues
  Scenario: Modals page meets WCAG 2.2 AA (excluding known third-party violations)
    Given I am on the modals page
    Then the page has no critical or serious accessibility violations excluding known issues

  @known_issues
  Scenario: Iframes page meets WCAG 2.2 AA (excluding third-party frame violations)
    Given I am on the iframes page
    Then the page has no critical or serious accessibility violations excluding cross-origin frame content

  @known_issues
  Scenario: Tables page meets WCAG 2.2 AA (excluding DataTables plugin violations)
    Given I am on the tables page
    Then the page has no critical or serious accessibility violations excluding DataTables controls
