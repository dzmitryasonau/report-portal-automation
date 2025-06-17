@regression @ui
Feature: Viewing test launches

  Background:
    Given a user with user name "AsDmitrij"
    And a project with name "dzmitry_asonau_personal"
    When I login in app

  @new_cucumber_0617 @used_UI_login
  Scenario: User check suites for launch with id 8931843
    When I open launches page
    And I open launch by it's id 8931843
    Then the following suites exists:
      | Suite with retries      |
      | Suite with nested steps |
      | beforeSuite             |
      | Filtering Launch Tests  |
    But the following suites absent:
      | Launch Tests      |
      | Test entity tests |
      | Permission tests  |
      | Sharing tests     |

  @used_UI_login
  Scenario: User check suites for launch with id 8931847
    When I open launches page
    And I open launch by it's id 8931847
    Then the following suites exists:
      | Suite with retries      |
      | Suite with nested steps |
      | beforeSuite             |
      | Filtering Launch Tests  |
      | beforeSuite             |
      | Launch Tests            |
      | beforeSuite             |
      | Test entity tests       |
      | beforeSuite             |
      | Permission tests        |
      | beforeSuite             |
      | Sharing tests           |
