@smoke
Feature: Practice Automation site availability
  As a test automation engineer
  I want a fast smoke check that the site under test is reachable
  So that deeper suites fail fast with a clear reason when it is not

  Scenario: Home page loads with its welcome heading
    Given I open the practice automation home page
    Then the welcome heading is displayed
