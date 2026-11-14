@forms
Feature: Form fields submission
  As a practice-site user
  I want to submit my details through the form
  So that my message is confirmed as received

  Background:
    Given I am on the form fields page

  @smoke
  Scenario: Submit a fully completed form
    When I complete the form with name "Darren" and message "Hello from the showcase suite"
    And I choose "Milk" as my drink and "Red" as my colour
    And I answer "yes" to liking automation
    And I submit the form
    Then I see the confirmation "Message received!"

  @regression
  Scenario Outline: Submit with different preference combinations
    When I complete the form with name "<name>" and message "<message>"
    And I choose "<drink>" as my drink and "<colour>" as my colour
    And I answer "<automation>" to liking automation
    And I submit the form
    Then I see the confirmation "Message received!"

    Examples:
      | name  | message                | drink  | colour | automation |
      | Alice | First example message  | Coffee | Blue   | no         |
      | Bob   | Second example message | Wine   | Green  | yes        |
      | Carol | Third example message  | Water  | Yellow | yes        |
