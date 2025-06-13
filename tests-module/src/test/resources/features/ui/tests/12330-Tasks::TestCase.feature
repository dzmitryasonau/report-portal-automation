Feature: Onboarding task to become familiar with test automation tasks

  Scenario: User logs in successfully
    Given Login data file received
    When the user goes to the login form
    And enters email address
    And enters account password
    And clicks on 'login'
    Then the user is logged in and forwarded to the app
