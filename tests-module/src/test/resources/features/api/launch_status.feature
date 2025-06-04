@api
Feature: Verify launch statuses

  Background:
    Given a random user
    And a project with name "dzmitry_asonau_personal"

  @used_API_connection
  Scenario: Check launches status via API
    When I get status of launches:
      | 8931843 |
      | 8931844 |
      | 8931845 |
      | 8931846 |
      | 8931847 |
    Then launch statuses correspond to the table:
      | launchId | launchStatus |
      | 8931843  | FAILED       |
      | 8931844  | FAILED       |
      | 8931845  | FAILED       |
      | 8931846  | FAILED       |
      | 8931847  | PASSED       |
