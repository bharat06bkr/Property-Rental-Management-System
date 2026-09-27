package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.MaintenancePriority;
import com.propertyrental.entity.Enums.MaintenanceStatus;
import java.time.LocalDateTime;

public class MaintenanceResponse {

    private Long id;
    private UserResponse tenant;
    private UserResponse owner;
    private PropertyResponse property;

    private String title;
    private String description;
    private MaintenancePriority priority;
    private MaintenanceStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public MaintenanceResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenancePriority getPriority() { return priority; }
    public void setPriority(MaintenancePriority priority) { this.priority = priority; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
