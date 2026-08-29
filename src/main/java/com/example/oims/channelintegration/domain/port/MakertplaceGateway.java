package com.example.oims.channelintegration.domain.port;

public interface MakertplaceGateway {
    // đồng bộ tồn kho lên sàn khi có thay đổi
    void pushStockUpdate(String sku, int newQuantity);
    // gửi call back về sàn
    void sendOrderConfirmation(String marketplaceOrderId, boolean accepted);
    // lấy tên sàn để log, debug
    String getChannelName();
}
