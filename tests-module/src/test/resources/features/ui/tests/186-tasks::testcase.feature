Feature: Simple test case 4

  Scenario: Verify the functionality of the Edit Dashboard button
    Given Ensure that the "Edit Dashboard" button is visible and functional
    When I log into ReportPortal using valid credentials
    And I open dashboards in ReportPortal
    And I locate the "Edit Dashboard" button
    And I click the "Edit Dashboard" button
    Then The Edit Dashboard button should function correctly.