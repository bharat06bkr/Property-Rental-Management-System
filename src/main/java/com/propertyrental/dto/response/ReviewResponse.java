package com.propertyrental.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private UserResponse tenant;
    private UserResponse owner;
    private PropertyResponse property;

    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
