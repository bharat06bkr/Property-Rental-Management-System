package com.propertyrental.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ApplicationRequest {

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @FutureOrPresent(message = "Move-in date must be today or in the future")
    private LocalDate preferredMoveInDate;

    private String message;

    public ApplicationRequest() {}

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public LocalDate getPreferredMoveInDate() { return preferredMoveInDate; }
    public void setPreferredMoveInDate(LocalDate preferredMoveInDate) { this.preferredMoveInDate = preferredMoveInDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
