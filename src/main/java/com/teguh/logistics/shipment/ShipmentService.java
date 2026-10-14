package com.teguh.logistics.shipment;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentTrackingRepository shipmentTrackingRepository;

    
    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentTrackingRepository shipmentTrackingRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentTrackingRepository = shipmentTrackingRepository;
    }

    public List<ShipmentResponse> getAllShipments() {

        List<Shipment> shipments = shipmentRepository.findAll();

        return shipments.stream()
            .map(ShipmentResponse::from)
            .toList();
    }

    public ShipmentResponse getShipment(Long id) {
        Shipment row = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id));

        return ShipmentResponse.from(row);
    }

    public ShipmentResponse createShipment(CreateShipmentRequest request) {

        Shipment shipment = new Shipment();

        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setOrigin(request.getOrigin());
        shipment.setDestination(request.getDestination());

        Shipment savedShipment = shipmentRepository.save(shipment);
        
        return ShipmentResponse.from(savedShipment);
    }

    public ShipmentResponse updateShipment(Long id, CreateShipmentRequest request) {

        Shipment shipment = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id));

        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setOrigin(request.getOrigin());
        shipment.setDestination(request.getDestination());

        Shipment savedShipment = shipmentRepository.save(shipment);
        
        return ShipmentResponse.from(savedShipment);
    }

    public void deleteShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id));
        
        shipmentRepository.delete(shipment);
    }

    @Transactional
    public void addTracking(Long shipmentId, CreateTrackingRequest request) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
        
        if (!isValidStatusTransition(shipment.getStatus(), request.getStatus())) {
            throw new InvalidShipmentStatusException(
                shipment.getStatus(), request.getStatus()
            );
        }
        shipment.setStatus(request.getStatus());
        
        ShipmentTracking track = new ShipmentTracking();
        
        track.setShipment(shipment);
        track.setStatus(request.getStatus());
        track.setDescription(request.getDescription());

        
        shipmentTrackingRepository.save(track);

    }

    public List<TrackingResponse> getTracking(Long shipmentId) {

        if (!shipmentRepository.existsById(shipmentId)) {
            throw new ShipmentNotFoundException(shipmentId);
        }
        List<ShipmentTracking> tracks = shipmentTrackingRepository.findByShipmentIdOrderByCreatedAtAsc(shipmentId);

        return tracks.stream()
            .map(TrackingResponse::from)
            .toList();
    }

    private boolean isValidStatusTransition(
            ShipmentStatus currentStatus,
            ShipmentStatus newStatus) {

        return switch (currentStatus) {

            case CREATED ->
                newStatus == ShipmentStatus.PICKED_UP ||
                newStatus == ShipmentStatus.CANCELLED;
            
            case PICKED_UP ->
                newStatus == ShipmentStatus.IN_TRANSIT ||
                newStatus == ShipmentStatus.CANCELLED;
            
            case IN_TRANSIT ->
                newStatus == ShipmentStatus.OUT_FOR_DELIVERY ||
                newStatus == ShipmentStatus.CANCELLED;
            
            case OUT_FOR_DELIVERY ->
                newStatus == ShipmentStatus.DELIVERED ||
                newStatus == ShipmentStatus.CANCELLED;

            case DELIVERED, CANCELLED -> false;

        };
    }
}