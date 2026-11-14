@popups @regression
Feature: JavaScript popups
  As a visitor to the practice site
  I want popups to behave predictably when accepted or dismissed
  So that automated journeys can rely on their outcomes

  Background:
    Given I am on the popups page

  Scenario: Alert popup informs the visitor
    When I open the alert popup
    Then the last dialog message is "Hi there, pal!"

  Scenario: Accepting the confirm popup records agreement
    When I accept the confirm popup
    Then the last dialog message is "OK or Cancel, which will it be?"
    And the page shows the result "OK it is!"

  Scenario: Dismissing the confirm popup records refusal
    When I dismiss the confirm popup
    Then the page shows the result "Cancel it is!"

  Scenario Outline: Answering the prompt popup greets the visitor by name
    When I answer the prompt popup with "<name>"
    Then the last dialog message is "Hi there, what's your name?"
    And the page shows the result "Nice to meet you, <name>!"

    Examples:
      | name         |
      | Darren       |
      | Ada Lovelace |
      | 999          |

  Scenario: Dismissing the prompt popup is handled gracefully
    When I dismiss the prompt popup
    Then the page shows the result "Fine, be that way"
