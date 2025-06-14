Feature: Simple test case 5
Precondition: Ensure that dashboards count is correct at the "Dashboard" page pagination

Scenario: Simple test case 5
When Log into ReportPortal using valid credentials
Then User is successfully logged into ReportPortal
When Open dashboards in ReportPortal
Then Dashboards page is displayed
When Count amount of available dashboards on the page
Then The count of available dashboards is displayed correctly