package com.teguh.logistics.shipment;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import com.teguh.logistics.exception.ApiErrorResponse;

@RestController
@RequestMapping("/api/shipments")
@Tag(
    name = "Shipments",
    description = "Manage shipments and their tracking history"
)
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Operation(
        summary = "Get all shipments",
        description = "Retrieve a list of all shipments"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Shipments retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = ShipmentResponse.class)
                )
            )
        )
    })
    @GetMapping
    public List<ShipmentResponse> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    
    @Operation(
        summary = "Create a new shipment",
        description = "Create a new shipment with initial status CREATED"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Shipment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ShipmentResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid shipment data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse createShipment(
        @Valid @RequestBody CreateShipmentRequest request
    ) {
        return shipmentService.createShipment(request);
    }

    @Operation(
        summary = "Get shipment by ID",
        description = "Retrieve shipment details using its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Shipment retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ShipmentResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Shipment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ShipmentResponse getShipment(@PathVariable Long id) {
        return shipmentService.getShipment(id);
    }


    @Operation(
        summary = "Update shipment details",
        description = "Update shipment tracking number, origin, and destination"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Shipment updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ShipmentResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid shipment data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Shipment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ShipmentResponse updateShipment(
            @PathVariable Long id,
            @Valid @RequestBody CreateShipmentRequest request) {

        return shipmentService.updateShipment(id, request);
    }

    @Operation(
        summary = "Delete shipment",
        description = "Delete an existing shipment by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Shipment deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Shipment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }

    @Operation(
        summary = "Add shipment tracking event",
        description = "Update shipment status and create a tracking history record. Status transitions must follow the shipment lifecycle."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Tracking event added successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid status transition or request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Shipment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping("/{id}/tracking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTracking(
            @PathVariable Long id,
            @Valid @RequestBody CreateTrackingRequest request) {

        shipmentService.addTracking(id, request);
    }

    @Operation(
        summary = "Get shipment tracking history",
        description = "Retrieve the chronological tracking history of a shipment"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tracking history retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = TrackingResponse.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Shipment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/{id}/tracking")
    public List<TrackingResponse> getTracking(@PathVariable Long id) {
        return shipmentService.getTracking(id);
    }

    
}