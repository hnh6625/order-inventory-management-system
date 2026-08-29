package com.example.oims.channelintegration.infrastructure.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopeeGatewayAdapterTest {
    private ShopeeGatewayAdapter adapter;

    @BeforeEach
    public void setup() {
        adapter = new ShopeeGatewayAdapter();
    }
    @Test
    void getChannelName_shouldReturnShopee() {
        assertEquals("SHOPEE", adapter.getChannelName());
    }
    @Test
    void pushStockUpdate_shouldNotThrow_whenCalled() {
        assertDoesNotThrow(() -> adapter.pushStockUpdate("BT001-M-WHT",50));
    }
    @Test
    void sendOrderConfirmation_shouldNotThrow_whenAccepted() {
        assertDoesNotThrow(() -> adapter.sendOrderConfirmation("SHOPEE",true));
    }
}
