Feature: Checkout process with PayPal integration
Precondition: User has items in the cart.

Scenario: Checkout process with PayPal integration
When Navigate to the cart and proceed to checkout
Then The checkout page is displayed
When Select PayPal as the payment option
Then PayPal is selected as the payment method
When Log in to PayPal and complete the payment
Then The payment is successfully processed
And The order confirmation page is displayed