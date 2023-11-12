@ui
Feature: Viewing test launches

  Background:
    Given a user with user name "AsDmitrij"
    And a project with name "dzmitry_asonau_personal"
    When I login in app

  @used_UI_login
  Scenario: User check suites for launch with id 6262801
    When I open launches page
    And I open launch by it's id 6262801
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
  Scenario: User check suites for launch with id 6262805
    When I open launches page
    And I open launch by it's id 6262805
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
