package com.teguh.logistics.shipment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
class ShipmentServiceIntegrationTest {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentTrackingRepository shipmentTrackingRepository;

    @Test
    void shouldLoadSpringContext() {
        assertNotNull(shipmentService);
        assertNotNull(shipmentRepository);
        assertNotNull(shipmentTrackingRepository);
    }

    @Test
    void shouldPersistShipmentStatusAndTrackingHistory() {

        // 1. Create shipment
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber("TEST-001");
        shipment.setOrigin("Jakarta");
        shipment.setDestination("Surabaya");

        Shipment savedShipment = shipmentRepository.save(shipment);

        // 2. Create tracking request
        CreateTrackingRequest request = new CreateTrackingRequest();
        request.setStatus(ShipmentStatus.PICKED_UP);
        request.setDescription("Package picked up from sender");

        // 3. Execute service
        shipmentService.addTracking(savedShipment.getId(), request);

        // 4. Read shipment again
        Shipment updatedShipment = shipmentRepository
            .findById(savedShipment.getId())
            .orElseThrow();

        // 5. Verify status
        assertEquals(
            ShipmentStatus.PICKED_UP,
            updatedShipment.getStatus()
        );

        // 6. Read tracking history
        List<ShipmentTracking> histories =
            shipmentTrackingRepository
                .findByShipmentIdOrderByCreatedAtAsc(savedShipment.getId());

        // 7. Verify tracking history
        assertEquals(1, histories.size());

        assertEquals(
            ShipmentStatus.PICKED_UP,
            histories.get(0).getStatus()
        );

        assertEquals(
            "Package picked up from sender",
            histories.get(0).getDescription()
        );
    }

    @Test
    void shouldRollbackShipmentStatusWhenTrackingInsertFails() {

        // 1. Create shipment
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber("TEST-ROLLBACK-001");
        shipment.setOrigin("Jakarta");
        shipment.setDestination("Bandung");

        Shipment savedShipment = shipmentRepository.save(shipment);

        // 2. Create request with invalid description
        CreateTrackingRequest request = new CreateTrackingRequest();
        request.setStatus(ShipmentStatus.PICKED_UP);
        request.setDescription(null);

        // 3. Tracking insert should fail
        assertThrows(
            DataIntegrityViolationException.class,
            () -> shipmentService.addTracking(savedShipment.getId(), request)
        );

        // 4. Read shipment again from database
        Shipment updatedShipment = shipmentRepository
            .findById(savedShipment.getId())
            .orElseThrow();

        // 5. Status should remain CREATED
        assertEquals(
            ShipmentStatus.CREATED,
            updatedShipment.getStatus()
        );

        // 6. No tracking history should be saved
        List<ShipmentTracking> histories =
            shipmentTrackingRepository
                .findByShipmentIdOrderByCreatedAtAsc(savedShipment.getId());

        assertEquals(0, histories.size());
    }
}