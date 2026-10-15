package com.teguh.logistics.shipment;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateShipmentRequest {

    @NotBlank(message = "Tracking number is required")
    @Schema(
        description = "Unique shipment tracking number",
        example = "SHIP-001"
    )
    private String trackingNumber;

    @NotBlank(message = "Origin is required")
    @Schema(
        description = "Shipment origin country or city",
        example = "Jakarta"
    )
    private String origin;

    @NotBlank(message = "Destination is required")
    @Schema(
        description = "Shipment destination country or city",
        example = "Surabaya"
    )
    private String destination;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}