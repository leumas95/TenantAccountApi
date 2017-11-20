Feature: Receipting

  Scenario: The provided example scenario - Rent Receipt
    Given that I have a tenant called Samuel who's rent is $300 a week
    And their current rent credit is $50
    And their current paid-to-date is 2016-10-14
    And they are registered in the account system
    When they pay $275
    Then their paid-to-date will be advanced to 2016-10-21
    And their rent credit will be updated to $25
    
  Scenario: Multiple Rent Receipts
    Given that I have a tenant called Brylie who's rent is $300 a week
    And their current rent credit is $50
    And their current paid-to-date is 2016-10-14
    And that I have a tenant called Simon who's rent is $550 a week
    And their current paid-to-date is 2017-11-07
    And they are registered in the account system
    When Brylie pays $275
    And Simon pays $1100
    And Simon pays $550
    Then Brylie's paid-to-date will be advanced to 2016-10-21
    And their rent credit will be updated to $25
    And Simon's paid-to-date will be advanced to 2017-11-28
    And their rent credit will be updated to $0
