package com.example.oims.channelintegration.infrastructure.client;

import com.example.oims.channelintegration.domain.port.MarketplaceGateway;

public class ShopeeGatewayAdapter implements MarketplaceGateway {

    @Override
    public void pushStockUpdate(String sku, int newQuantity) {
        // giả lập gọi API Shopee
        System.out.println("[SHOPEE] Stock update: " + sku + " = " + newQuantity);
    }

    @Override
    public void sendOrderConfirmation(String marketplaceOrderId, boolean accepted) {
        System.out.println("[SHOPEE] Order " + marketplaceOrderId
                + (accepted ? " CONFIRMED" : " REJECTED"));
    }

    @Override
    public String getChannelName() {
        return "SHOPEE";
    }
}
