@iframes @regression
Feature: Embedded frames
  As a test automation engineer
  I want to interact with content embedded in cross-origin iframes
  So that journeys spanning embedded components (like GOV.UK One Login) are testable

  Background:
    Given I am on the iframes page

  Scenario: Playwright documentation frame loads and navigates in isolation
    Then the Playwright frame shows its introduction
    And the parent page still shows "Me too!" outside the frame
    When I open the docs inside the Playwright frame
    Then the Playwright frame shows the "Installation" page

  Scenario: Selenium documentation frame loads and navigates
    Then the Selenium frame shows its introduction
    When I open the downloads page inside the Selenium frame
    Then the Selenium frame shows the "Downloads" page
