Feature: Verify page responsiveness
Precondition: Browser supports responsive testing.

Scenario: Verify page responsiveness
When Open the application on a desktop screen size
Then All UI elements are rendered correctly on the desktop screen size
When Open the application on a tablet screen size
Then All UI elements are rendered correctly on the tablet screen size
When Open the application on a mobile screen size
Then All UI elements are rendered correctly on the mobile screen size
