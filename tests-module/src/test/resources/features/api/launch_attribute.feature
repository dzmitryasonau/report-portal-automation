@api
Feature: Update launch attributes

  Background:
    Given a random user
    And a project with name "dzmitry_asonau_personal"

  @used_API_connection
  Scenario Outline: User update launch attributes via API

    When I send request to update attribute with launch id <launchID>
    Then response message about updating attribute of launch with id <launchID> is correct

    Examples:
      | launchID |
      | 6262801  |
      | 6262802  |
      | 6262803  |
      | 6262804  |
      | 6262805  |
