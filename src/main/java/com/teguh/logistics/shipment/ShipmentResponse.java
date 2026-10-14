package com.teguh.logistics.shipment;

public class ShipmentResponse {

    private Long id;
    private String trackingNumber;
    private String origin;
    private String destination;
    private ShipmentStatus status;

    public ShipmentResponse(
            Long id,
            String trackingNumber,
            String origin,
            String destination,
            ShipmentStatus status) {

        this.id = id;
        this.trackingNumber = trackingNumber;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
    }

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
            shipment.getId(),
            shipment.getTrackingNumber(),
            shipment.getOrigin(),
            shipment.getDestination(),
            shipment.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
    public ShipmentStatus getStatus() {
        return status;
    }
}