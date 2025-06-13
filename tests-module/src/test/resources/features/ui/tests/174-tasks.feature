Feature: Simple test case 2

  Background:
    Given The search bar in the Dashboard page of the ReportPortal is functional and accepts user input

  Scenario: Search for a keyword in the Dashboard page
    When Log into ReportPortal using valid credentials
    Then The user is successfully logged into ReportPortal
    When Open the Dashboard page
    Then The Dashboard page is displayed
    When Enter the keyword "DEMO" into the search bar
    Then The keyword "DEMO" is entered into the search bar
    When Press Enter or click the search button
    Then The search results for "DEMO" are displayed