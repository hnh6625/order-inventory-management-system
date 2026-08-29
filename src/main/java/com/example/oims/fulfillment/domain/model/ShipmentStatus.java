package com.example.oims.fulfillment.domain.model;

public enum ShipmentStatus {
    PENDING,      // vừa tạo, chưa lấy hàng
    PICKED_UP,    // đã lấy hàng
    DELIVERING,   // đang giao
    DELIVERED,    // giao thành công
    FAILED        // giao thất bại

}
