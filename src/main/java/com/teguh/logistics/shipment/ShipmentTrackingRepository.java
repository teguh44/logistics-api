package com.teguh.logistics.shipment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentTrackingRepository
        extends JpaRepository<ShipmentTracking, Long> {

    List<ShipmentTracking> findByShipmentIdOrderByCreatedAtAsc(Long shipmentId);
}