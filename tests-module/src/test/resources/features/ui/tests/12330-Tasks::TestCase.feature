Feature: Onboarding task to become familiar with test automation tasks
  
  Scenario: User logs in and is forwarded to the app
    Given Login data file is received
    When Go to the login form
    Then The login form is displayed
    When Enter email address
    Then The email address is entered
    When Enter account password
    Then The account password is entered
    When Click on 'login'
    Then User is logged in and forwarded to the app
