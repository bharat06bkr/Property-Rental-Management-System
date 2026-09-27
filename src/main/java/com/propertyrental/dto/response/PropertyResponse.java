package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.ApprovalStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class PropertyResponse {

    private Long id;
    private UserResponse owner;
    private LocationResponse location;
    private PropertyTypeResponse propertyType;
    private Set<AmenityResponse> amenities;

    private String title;
    private String description;
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double areaSqFt;
    private String furnishingStatus;
    private Integer floorNumber;
    private Integer totalFloors;
    private Boolean parkingAvailable;
    private Boolean petAllowed;
    private String waterSupply;
    private String electricityAvailable;

    private ApprovalStatus approvalStatus;
    private AvailabilityStatus availabilityStatus;
    private String rejectionReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PropertyResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public LocationResponse getLocation() { return location; }
    public void setLocation(LocationResponse location) { this.location = location; }

    public PropertyTypeResponse getPropertyType() { return propertyType; }
    public void setPropertyType(PropertyTypeResponse propertyType) { this.propertyType = propertyType; }

    public Set<AmenityResponse> getAmenities() { return amenities; }
    public void setAmenities(Set<AmenityResponse> amenities) { this.amenities = amenities; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }

    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }

    public Integer getBathrooms() { return bathrooms; }
    public void setBathrooms(Integer bathrooms) { this.bathrooms = bathrooms; }

    public Double getAreaSqFt() { return areaSqFt; }
    public void setAreaSqFt(Double areaSqFt) { this.areaSqFt = areaSqFt; }

    public String getFurnishingStatus() { return furnishingStatus; }
    public void setFurnishingStatus(String furnishingStatus) { this.furnishingStatus = furnishingStatus; }

    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }

    public Integer getTotalFloors() { return totalFloors; }
    public void setTotalFloors(Integer totalFloors) { this.totalFloors = totalFloors; }

    public Boolean getParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(Boolean parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    public Boolean getPetAllowed() { return petAllowed; }
    public void setPetAllowed(Boolean petAllowed) { this.petAllowed = petAllowed; }

    public String getWaterSupply() { return waterSupply; }
    public void setWaterSupply(String waterSupply) { this.waterSupply = waterSupply; }

    public String getElectricityAvailable() { return electricityAvailable; }
    public void setElectricityAvailable(String electricityAvailable) { this.electricityAvailable = electricityAvailable; }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }

    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
