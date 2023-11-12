@api
Feature: Verify launch statuses

  Background:
    Given a random user
    And a project with name "dzmitry_asonau_personal"

  @used_API_connection
  Scenario: Check launches status via API
    When I get status of launches:
      | 6262801 |
      | 6262802 |
      | 6262803 |
      | 6262804 |
      | 6262805 |
    Then launch statuses correspond to the table:
      | launchId | launchStatus |
      | 6262801  | FAILED       |
      | 6262802  | FAILED       |
      | 6262803  | FAILED       |
      | 6262804  | FAILED       |
      | 6262805  | PASSED       |
