Feature: Add to cart functionality
Precondition: The user is logged into the application.

Scenario: Add a product to the cart and verify its details
When Navigate to the product listing page
Then The product listing page is displayed
When Select a product
And Click on the 'Add to Cart' button
Then The product is added to the cart
When Navigate to the cart
Then The product details in the cart are correct