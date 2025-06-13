Feature: Onboarding task to become familiar with test automation tasks

  Scenario: User logs in successfully
    Given Login data file received
    When Go to the login form
    And Enter email address
    And Enter account password
    And Click on 'login'
    Then User is logged in and forwarded to the app
