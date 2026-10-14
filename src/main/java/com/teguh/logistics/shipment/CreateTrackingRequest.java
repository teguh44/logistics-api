package com.teguh.logistics.shipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTrackingRequest {

    @NotNull(message = "Status is required")
    private ShipmentStatus status;

    @NotBlank(message = "Description is required")
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