package com.teguh.logistics.shipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateTrackingRequest {

    @NotNull(message = "Status is required")
    @Schema(
        description = "New shipment status following the allowed lifecycle",
        example = "PICKED_UP"
    )
    private ShipmentStatus status;

    @NotBlank(message = "Description is required")
    @Schema(
        description = "Description of the tracking event",
        example = "Package picked up from sender"
    )
    private String description;

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}