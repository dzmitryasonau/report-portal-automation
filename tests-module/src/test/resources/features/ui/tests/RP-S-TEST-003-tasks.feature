Feature: Simple test case 3
Precondition: The "Add Widget" button is visible on the dashboard page

Scenario: Verify the presence of the "Add Widget" button on the dashboard
When Navigate to any dashboard page
Then The dashboard page is displayed
When Look for the "Add Widget" button in the top-right corner of the page
Then The "Add Widget" button is visible