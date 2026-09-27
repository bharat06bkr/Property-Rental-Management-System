package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.ApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private UserResponse tenant;
    private PropertyResponse property;
    private LocalDate applicationDate;
    private LocalDate preferredMoveInDate;
    private String message;
    private ApplicationStatus status;
    private LocalDateTime createdAt;

    public ApplicationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public LocalDate getPreferredMoveInDate() { return preferredMoveInDate; }
    public void setPreferredMoveInDate(LocalDate preferredMoveInDate) { this.preferredMoveInDate = preferredMoveInDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
