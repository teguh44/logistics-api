package com.teguh.logistics.shipment;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<ShipmentResponse> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse createShipment(
        @Valid @RequestBody CreateShipmentRequest request
    ) {
        return shipmentService.createShipment(request);
    }

    @GetMapping("/{id}")
    public ShipmentResponse getShipment(@PathVariable Long id) {
        return shipmentService.getShipment(id);
    }

    @PutMapping("/{id}")
    public ShipmentResponse updateShipment(@PathVariable Long id, @Valid @RequestBody CreateShipmentRequest request) {
        return shipmentService.updateShipment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }

    @PostMapping("/{id}/tracking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTracking(@PathVariable Long id, @Valid @RequestBody CreateTrackingRequest request) {
        shipmentService.addTracking(id, request);
    }

    @GetMapping("/{id}/tracking")
    public List<TrackingResponse> getTracking(@PathVariable Long id) {
        return shipmentService.getTracking(id);
    }

    
}