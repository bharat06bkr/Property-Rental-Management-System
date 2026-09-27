package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.ComplaintStatus;
import java.time.LocalDateTime;

public class ComplaintResponse {

    private Long id;
    private UserResponse tenant;
    private PropertyResponse property;
    private UserResponse owner;

    private String title;
    private String description;
    private ComplaintStatus status;
    private String adminResponse;

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public ComplaintResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
