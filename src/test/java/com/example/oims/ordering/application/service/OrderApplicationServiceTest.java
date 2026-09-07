package com.example.oims.ordering.application.service;

import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.ordering.domain.repository.OrderRepository;
import com.example.oims.ordering.infrastructure.web.dto.OrderLineRequest;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderApplicationServiceTest {
    private OrderRepository orderRepository;
    private StockReservationService stockReservationService;
    private OrderApplicationService service;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        stockReservationService = Mockito.mock(StockReservationService.class);

        service = new OrderApplicationService(
                orderRepository,
                stockReservationService
        );
    }

    @Test
    void cancelOrder_shouldReleaseReservedStock() {
        UUID orderId = UUID.randomUUID();

        OrderLineRequest line = new OrderLineRequest(
                "BT001-WHT-M",
                3,
                new BigDecimal("100000")
        );

        Order order = new Order(
                "ORDER-001",
                "SHOPEE",
                List.of(
                        new OrderLine(
                                SKU.of(line.sku()),
                                line.quantity(),
                                new Money(line.unitPrice())
                        )
                )
        );

        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        service.cancelOrder(orderId);

        Mockito.verify(stockReservationService)
                .release(
                        SKU.of("BT001-WHT-M"),
                        3
                );

        Mockito.verify(orderRepository)
                .save(order);

        assertEquals(
                com.example.oims.ordering.domain.model.OrderStatus.CANCELLED,
                order.getStatus()
        );
    }
    @Test
    void cancelOrder_shouldNotReleaseStock_whenOrderIsShipped() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order(
                "ORDER-002",
                "SHOPEE",
                List.of(
                        new OrderLine(
                                SKU.of("BT001-WHT-M"),
                                3,
                                new Money(new BigDecimal("100000"))
                        )
                )
        );

        order.confirm();
        order.pack(com.example.oims.ordering.domain.model.FulfillmentType.SELF_ARRANGED);
        order.ship();

        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidStateTransitionException.class,
                () -> service.cancelOrder(orderId)
        );

        Mockito.verify(stockReservationService, Mockito.never())
                .release(Mockito.any(), Mockito.anyInt());

        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any());

        assertEquals(
                com.example.oims.ordering.domain.model.OrderStatus.SHIPPED,
                order.getStatus()
        );
    }

    @Test
    void cancelOrder_shouldReleaseStockForAllOrderLines() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order(
                "ORDER-003",
                "SHOPEE",
                List.of(
                        new OrderLine(
                                SKU.of("BT001-WHT-M"),
                                3,
                                new Money(new BigDecimal("100000"))
                        ),
                        new OrderLine(
                                SKU.of("BT001-BLK-L"),
                                2,
                                new Money(new BigDecimal("120000"))
                        )
                )
        );

        Mockito.when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        service.cancelOrder(orderId);

        Mockito.verify(stockReservationService)
                .release(SKU.of("BT001-WHT-M"), 3);

        Mockito.verify(stockReservationService)
                .release(SKU.of("BT001-BLK-L"), 2);

        Mockito.verify(orderRepository)
                .save(order);

        assertEquals(
                com.example.oims.ordering.domain.model.OrderStatus.CANCELLED,
                order.getStatus()
        );
    }
}