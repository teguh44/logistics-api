package com.teguh.logistics.shipment;

import java.time.LocalDateTime;

public class TrackingResponse {

    private Long id;
    private ShipmentStatus status;
    private String description;
    private LocalDateTime createdAt;

    public TrackingResponse(
            Long id,
            ShipmentStatus status,
            String description,
            LocalDateTime createdAt) {

        this.id = id;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static TrackingResponse from(ShipmentTracking tracking) {
        return new TrackingResponse(
            tracking.getId(),
            tracking.getStatus(),
            tracking.getDescription(),
            tracking.getCreatedAt()
        );
    }
}