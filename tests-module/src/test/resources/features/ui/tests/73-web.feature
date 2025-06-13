Feature: Simple test case 4
Precondition: Ensure that the "Edit Dashboard" button is visible and functional

Scenario: Perform actions to edit a dashboard
When Log into ReportPortal using valid credentials
Then User is successfully logged in
When Open dashboards in ReportPortal
Then Dashboards page is displayed
When Locate the "Edit Dashboard" button
Then "Edit Dashboard" button is visible
When Click the "Edit Dashboard" button
Then Dashboard editing interface is displayed