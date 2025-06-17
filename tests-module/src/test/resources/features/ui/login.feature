@ui
Feature: Login to UI

  Rule: Users can login with valid credentials

    @new_cucumber_0617 @used_UI_login
    Example: User with project manager role logins to UI

      Given I am logged in as "AsDmitrij"
      When I open block with userinfo
      Then Name of current user is "AsDmitrij"

    @used_UI_login
    Example: Invited user with member role logins to UI

      Given I am logged in as "AsDmitryj"
      When I open block with userinfo
      Then Name of current user is "AsDmitryj"

  Rule: Users can't login with invalid credentials

    @used_UI_login
    Scenario Outline: User cannot login with invalid password
      When I open login page
      And I fill login <userName>
      And I fill password "incorrectPass"
      And I click on login button
      Then User should see an error message

      Examples:
        | userName  |
        | "AsDmitryj"  |
        | "AsDmitrij"  |

    @used_UI_login
    Example: User cannot login with invalid username
      When I open login page
      And I fill login "testUser1"
      And I fill password "correctPass"
      And I click on login button
      Then User should see an error message
