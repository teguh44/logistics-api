package com.teguh.logistics.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidShipmentStatusException extends RuntimeException {

    public InvalidShipmentStatusException(
            ShipmentStatus currentStatus,
            ShipmentStatus newStatus) {

        super(
            "Cannot change shipment status from "
            + currentStatus
            + " to "
            + newStatus
        );
    }
}