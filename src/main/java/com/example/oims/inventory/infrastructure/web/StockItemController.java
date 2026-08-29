package com.example.oims.inventory.infrastructure.web;

import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.inventory.infrastructure.web.dto.RestockRequest;
import com.example.oims.inventory.infrastructure.web.dto.StockItemResponse;
import com.example.oims.shared.SKU;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class StockItemController {

    private final StockReservationService stockReservationService;

    public StockItemController(StockReservationService stockReservationService) {
        this.stockReservationService = stockReservationService;
    }

    @PostMapping("/restock")
    public ResponseEntity<Void> restock(@RequestBody @Valid RestockRequest request ) {
        stockReservationService.restock(
                SKU.of(request.sku()),
                request.quantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{sku}")
    public ResponseEntity<StockItemResponse> getStock(@PathVariable String sku) {
        StockItem stockItem = stockReservationService.findBySku(SKU.of(sku));
        StockItemResponse response = StockItemResponse.from(stockItem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StockItemResponse>> getAllStock() {
        List<StockItemResponse> responses = stockReservationService.findAll()
                .stream()
                .map(StockItemResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
