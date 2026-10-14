package com.teguh.logistics.shipment;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentTrackingRepository shipmentTrackingRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    void shouldThrowExceptionWhenShipmentNotFound() {

        Long shipmentId = 999L;

        when(shipmentRepository.findById(shipmentId))
            .thenReturn(Optional.empty());

        assertThrows(
            ShipmentNotFoundException.class,
            () -> shipmentService.getShipment(shipmentId)
        );
    }

    @Test
    void shouldRejectInvalidStatusTransition() {

        Long shipmentId = 3L;

        Shipment shipment = new Shipment();
        shipment.setStatus(ShipmentStatus.CREATED);

        CreateTrackingRequest request = new CreateTrackingRequest();
        request.setStatus(ShipmentStatus.DELIVERED);
        request.setDescription("Trying to skip status");

        when(shipmentRepository.findById(shipmentId))
            .thenReturn(Optional.of(shipment));

        assertThrows(
            InvalidShipmentStatusException.class,
            () -> shipmentService.addTracking(shipmentId, request)
        );
    }

    @Test
    void shouldUpdateShipmentAndCreateTrackingWhenTransitionIsValid() {

        Long shipmentId = 3L;

        Shipment shipment = new Shipment();
        shipment.setStatus(ShipmentStatus.CREATED);

        CreateTrackingRequest request = new CreateTrackingRequest();
        request.setStatus(ShipmentStatus.PICKED_UP);
        request.setDescription("Package picked up from sender");

        when(shipmentRepository.findById(shipmentId))
            .thenReturn(Optional.of(shipment));

        shipmentService.addTracking(shipmentId, request);

        assertEquals(ShipmentStatus.PICKED_UP, shipment.getStatus());

        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository, never()).save(any(Shipment.class));
        ArgumentCaptor<ShipmentTracking> trackingCaptor =
            ArgumentCaptor.forClass(ShipmentTracking.class);

        verify(shipmentTrackingRepository)
            .save(trackingCaptor.capture());

        ShipmentTracking savedTracking = trackingCaptor.getValue();
        assertEquals(
            ShipmentStatus.PICKED_UP,
            savedTracking.getStatus()
        );

        assertEquals(
            "Package picked up from sender",
            savedTracking.getDescription()
        );

        assertSame(
            shipment,
            savedTracking.getShipment()
        );
    }
}