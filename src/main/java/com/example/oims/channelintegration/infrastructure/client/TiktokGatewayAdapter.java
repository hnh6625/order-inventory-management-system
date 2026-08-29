package com.example.oims.channelintegration.infrastructure.client;

import com.example.oims.channelintegration.domain.port.MarketplaceGateway;

public class TiktokGatewayAdapter implements MarketplaceGateway {
    @Override
    public void pushStockUpdate(String sku, int newQuantity) {
        // giả lập gọi API Tiktok
        System.out.println("[TIKTOK] Stock update: " + sku + " = " + newQuantity);
    }

    @Override
    public void sendOrderConfirmation(String marketplaceOrderId, boolean accepted) {
        System.out.println("[TIKTOK] Order " + marketplaceOrderId
                + (accepted ? " CONFIRMED" : " REJECTED"));
    }

    @Override
    public String getChannelName() {
        return "TIKTOK_SHOP";
    }
}
