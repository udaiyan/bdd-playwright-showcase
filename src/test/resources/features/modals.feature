@modals @regression
Feature: Modal dialogs
  As a visitor to the practice site
  I want modals to present information and collect contact details
  So that automated journeys can rely on their open, close and validation behaviour

  Background:
    Given I am on the modals page

  Scenario: Simple modal presents its message and can be closed
    When I open the simple modal
    Then the simple modal shows the title "Simple Modal"
    And the simple modal shows the message "Hi, I'm a simple modal."
    When I close the visible modal
    Then the simple modal is no longer visible

  Scenario: Form modal presents the contact form
    When I open the form modal
    Then the form modal is visible
    And the form modal shows a name, email and message field

  Scenario: Form modal requires a name before submitting
    When I open the form modal
    And I submit the form modal without entering a name
    Then the form modal shows the error "This field is required."

  Scenario: Form modal accepts a valid contact submission
    When I open the form modal
    And I complete the form modal with name "Darren", email "darren@example.com" and message "Showcase submission"
    And I submit the form modal
    Then the submission is confirmed
