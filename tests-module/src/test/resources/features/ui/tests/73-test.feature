Feature: Simple test case 4

  Scenario: Verify Edit Dashboard button functionality
    Given Ensure that the "Edit Dashboard" button is visible and functional
    When I log into ReportPortal using valid credentials
    And I open dashboards in ReportPortal
    And I locate the "Edit Dashboard" button
    And I click the "Edit Dashboard" button
    Then I should see the Edit Dashboard functionality