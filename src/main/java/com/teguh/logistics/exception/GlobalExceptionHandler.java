package com.teguh.logistics.exception;

import jakarta.servlet.http.HttpServletRequest;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.teguh.logistics.shipment.InvalidShipmentStatusException;
import com.teguh.logistics.shipment.ShipmentNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error ->
                error.getField() + ": " + error.getDefaultMessage()
            )
            .collect(Collectors.joining(", "));

        ApiErrorResponse error = new ApiErrorResponse(
            400,
            "Bad Request",
            message,
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidShipmentStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidShipmentStatus(
            InvalidShipmentStatusException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
            400,
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
            400,
            "Bad Request",
            "Invalid request body",
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleShipmentNotFound(
            ShipmentNotFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
            404,
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }
}