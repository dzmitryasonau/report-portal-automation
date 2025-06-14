Feature: Simple test case 2
Precondition: Ensure the search bar in Dashboard page of the ReportPortal is functional and accepts user input

Scenario: Simple test case 2
When Log into ReportPortal using valid credentials
Then User is successfully logged into ReportPortal
When Open Dashboard page
Then Dashboard page is displayed
When Enter the keyword "DEMO" into the search bar
Then The keyword "DEMO" is entered into the search bar
When Press Enter or click the search button
Then Search results related to "DEMO" are displayed