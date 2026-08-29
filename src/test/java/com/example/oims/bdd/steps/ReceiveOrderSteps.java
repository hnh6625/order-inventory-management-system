package com.example.oims.bdd.steps;

import com.example.oims.bdd.fakes.InMemoryOrderRepository;
import com.example.oims.bdd.fakes.InMemoryStockItemRepository;
import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.ordering.domain.model.*;
import com.example.oims.ordering.infrastructure.web.dto.OrderLineRequest;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.StockItemNotFoundException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiveOrderSteps {

    private InMemoryStockItemRepository stockItemRepository
            = new InMemoryStockItemRepository();
    private InMemoryOrderRepository orderRepository
            = new InMemoryOrderRepository();

    private StockReservationService stockReservationService
            = new StockReservationService(stockItemRepository);

    private Order createdOrder;
    private Exception thrownException;

    private void processWebhook(String marketplaceOrderId, String sku, int quantity) {
        boolean alreadyProcessed = orderRepository
                .findByMarketplaceOrderId(marketplaceOrderId)
                .isPresent();

        if (alreadyProcessed) {
            return;
        }

        try {
            List<OrderLineRequest> requests = List.of(
                    new OrderLineRequest(sku, quantity, new BigDecimal("150000"))
            );
            createdOrder = OrderFactory.create(marketplaceOrderId, "SHOPEE", requests);
            stockReservationService.reserve(SKU.of(sku),quantity);
            orderRepository.save(createdOrder);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Given("product {string} has {int} units in stock")
    public void productHasUnitsInStock(String sku, int quantity) {
        StockItem stockItem = new StockItem(SKU.of(sku), quantity);
        stockItemRepository.save(stockItem);
    }

    @When("Shopee sends a webhook order for {int} units of {string}")
    public void shopeeSendsWebhookOrder(int quantity, String sku) {
        processWebhook("SP-001", sku, quantity);
    }

    @Then("the internal order should be created with status {string}")
    public void orderShouldBeCreatedWithStatus(String expectedStatus) {
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.valueOf(expectedStatus), createdOrder.getStatus());
    }

    @And("the stock for {string} should be {int} units")
    public void stockShouldBe(String sku, int expectedQuantity) {
        StockItem stock = stockItemRepository.findBySku(SKU.of(sku))
                .orElseThrow();
        assertEquals(expectedQuantity, stock.getQuantity());
    }

    @Then("the order should be rejected with reason {string}")
    public void orderShouldBeRejected(String reason) {
        assertNotNull(thrownException);
        assertInstanceOf(StockItemNotFoundException.class, thrownException);
    }

    @And("the stock for {string} should remain {int} units")
    public void stockShouldRemain(String sku, int expectedQuantity) {
        stockShouldBe(sku, expectedQuantity);
    }

    @When("Shopee sends a webhook order {string} for {int} units of {string}")
    public void shopeesendsWebhookOrderWithId(
            String marketplaceOrderId, int quantity, String sku) {
        processWebhook(marketplaceOrderId, sku, quantity);
    }

    @When("Shopee sends the same webhook {string} again")
    public void shopeeSendsTheSameWebhookAgain(String marketplaceOrderId) {
        OrderLine line = createdOrder.getOrderLines().get(0);

        String sku = line.getSku().toString();
        int quantity = line.getQuantity();
        processWebhook(marketplaceOrderId, sku, quantity);
    }

    @Then("only one order should exist with marketplace order id {string}")
    public void onlyOneOrderShouldExist(String marketplaceOrderId) {
        long count = orderRepository.findAll()
                .stream()
                .filter(o -> o.getMarketplaceOrderId().equals(marketplaceOrderId))
                .count();

        assertEquals(1, count);
    }
}