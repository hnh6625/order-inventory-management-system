    package com.example.oims.fulfillment.infrastructure.web;
    
    import com.example.oims.fulfillment.infrastructure.web.dto.CreateShipmentRequest;
    import com.example.oims.fulfillment.infrastructure.web.dto.RecordShipmentRequest;
    import com.example.oims.fulfillment.infrastructure.web.dto.ShipmentResponse;
    import com.example.oims.fulfillment.application.service.FulfillmentApplicationService;
    import com.example.oims.fulfillment.domain.model.Shipment;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.UUID;

    @RestController
    @RequestMapping("/api/fulfillment")
    public class FulfillmentController {
    
        private final FulfillmentApplicationService fulfillmentApplicationService;
    
        public FulfillmentController(FulfillmentApplicationService fulfillmentApplicationService) {
            this.fulfillmentApplicationService = fulfillmentApplicationService;
        }
    
        @PostMapping("/shipments")
        public ResponseEntity<ShipmentResponse> createShipment(@RequestBody CreateShipmentRequest request) {
            Shipment shipment = fulfillmentApplicationService
                    .createShipment(request.orderId(),request.fulfillmentType());
            ShipmentResponse response = ShipmentResponse.from(shipment);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }


        @PostMapping("/shipments/{id}/record")
        public ResponseEntity<Void> recordShipment(@PathVariable UUID id,
                                                   @RequestBody RecordShipmentRequest request) {
            fulfillmentApplicationService.recordSelfArrangedShipment(
                    id,
                    request.carrierName(),
                    request.trackingCode());
            return  ResponseEntity.ok().build();
        }

        @PostMapping("/shipments/{id}/delivered")
        public  ResponseEntity<Void> confirmDelivered(@PathVariable UUID id) {
            fulfillmentApplicationService.confirmDelivered(id);
            return  ResponseEntity.ok().build();
        }

        @PostMapping("/shipments/{id}/failed")
        public  ResponseEntity<Void> confirmFailed(@PathVariable UUID id) {
            fulfillmentApplicationService.confirmFailed(id);
            return  ResponseEntity.ok().build();
        }

        @GetMapping("/shipments/order/{orderId}")
        public ResponseEntity<ShipmentResponse> getShipmentByOrderId(@PathVariable UUID orderId) {
            Shipment shipment = fulfillmentApplicationService
                    .getShipmentByOrderId(orderId);
            return ResponseEntity.ok(ShipmentResponse.from(shipment));
        }
    }
