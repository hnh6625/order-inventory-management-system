Feature: Receive order from marketplace
  As a marketplace system
  I want to send others to MOC's internal system via webhook
  So that inventory and order status stay synced

  Background:
    Given product "BT001-WHT-M" has 10 units in stock

  Scenario: Marketplace sends an order with sufficient stock
    When Shopee sends a webhook order for 3 units of "BT001-WHT-M"
    Then the internal order should be created with status "RESERVED"
    And the stock for "BT001-WHT-M" should be 7 units

  Scenario: Marketplace sends an order with insufficient stock
    When Shopee sends a webhook order for 15 units of "BT001-WTH-M"
    Then the order should be rejected with reason "INSUFFICIENT_STOCK"
    And the stock for "BT001-WHT-M" should remain 10 units

  Scenario: Marketplace retries the same webhook (idempotency)
    When Shopee sends a webhook order "SP-001" for 3 units of "BT001-WHT-M"
    And Shopee sends the same webhook "SP-001" again
    Then only one order should exist with marketplace order id "SP-001"
    And the stock for "BT001-WHT-M" should be 7 units