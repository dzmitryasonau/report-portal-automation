Feature: Simple test case 3
Precondition: Ensure the "Add Widget" button is visible on the dashboard page

Scenario: Navigate to the dashboard and verify the "Add Widget" button is visible
  When Navigate to any dashboard page
  Then The "Add Widget" button is visible in the top-right corner of the page