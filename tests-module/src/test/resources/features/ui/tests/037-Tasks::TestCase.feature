Feature: File upload feature test
Precondition: The file upload feature is enabled.

Scenario: File upload feature test
When Navigate to the file upload page
Then The file upload page is displayed
When Click on the 'Choose File' button
And Select a valid file
Then The selected file is displayed in the file input field
When Click on the 'Upload' button
Then The file is successfully uploaded
And The uploaded file is listed on the page