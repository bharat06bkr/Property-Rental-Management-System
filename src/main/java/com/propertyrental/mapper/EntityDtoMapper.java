package com.propertyrental.mapper;

import com.propertyrental.dto.response.*;
import com.propertyrental.entity.*;

import java.util.Collections;
import java.util.stream.Collectors;

public class EntityDtoMapper {

    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setOwnerStatus(user.getOwnerStatus());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static LocationResponse toLocationResponse(Location location) {
        if (location == null) return null;
        return new LocationResponse(
                location.getId(),
                location.getCity(),
                location.getArea(),
                location.getState(),
                location.getPincode()
        );
    }

    public static PropertyTypeResponse toPropertyTypeResponse(PropertyType type) {
        if (type == null) return null;
        return new PropertyTypeResponse(type.getId(), type.getName(), type.getDescription());
    }

    public static AmenityResponse toAmenityResponse(Amenity amenity) {
        if (amenity == null) return null;
        return new AmenityResponse(amenity.getId(), amenity.getName(), amenity.getDescription());
    }

    public static PropertyResponse toPropertyResponse(Property property) {
        if (property == null) return null;
        PropertyResponse dto = new PropertyResponse();
        dto.setId(property.getId());
        dto.setOwner(toUserResponse(property.getOwner()));
        dto.setLocation(toLocationResponse(property.getLocation()));
        dto.setPropertyType(toPropertyTypeResponse(property.getPropertyType()));
        if (property.getAmenities() != null) {
            dto.setAmenities(property.getAmenities().stream()
                    .map(EntityDtoMapper::toAmenityResponse)
                    .collect(Collectors.toSet()));
        } else {
            dto.setAmenities(Collections.emptySet());
        }
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setMonthlyRent(property.getMonthlyRent());
        dto.setSecurityDeposit(property.getSecurityDeposit());
        dto.setBedrooms(property.getBedrooms());
        dto.setBathrooms(property.getBathrooms());
        dto.setAreaSqFt(property.getAreaSqFt());
        dto.setFurnishingStatus(property.getFurnishingStatus());
        dto.setFloorNumber(property.getFloorNumber());
        dto.setTotalFloors(property.getTotalFloors());
        dto.setParkingAvailable(property.getParkingAvailable());
        dto.setPetAllowed(property.getPetAllowed());
        dto.setWaterSupply(property.getWaterSupply());
        dto.setElectricityAvailable(property.getElectricityAvailable());
        dto.setApprovalStatus(property.getApprovalStatus());
        dto.setAvailabilityStatus(property.getAvailabilityStatus());
        dto.setRejectionReason(property.getRejectionReason());
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());
        return dto;
    }

    public static ApplicationResponse toApplicationResponse(RentalApplication app) {
        if (app == null) return null;
        ApplicationResponse dto = new ApplicationResponse();
        dto.setId(app.getId());
        dto.setTenant(toUserResponse(app.getTenant()));
        dto.setProperty(toPropertyResponse(app.getProperty()));
        dto.setApplicationDate(app.getApplicationDate());
        dto.setPreferredMoveInDate(app.getPreferredMoveInDate());
        dto.setMessage(app.getMessage());
        dto.setStatus(app.getStatus());
        dto.setCreatedAt(app.getCreatedAt());
        return dto;
    }

    public static AgreementResponse toAgreementResponse(RentalAgreement agreement) {
        if (agreement == null) return null;
        AgreementResponse dto = new AgreementResponse();
        dto.setId(agreement.getId());
        if (agreement.getApplication() != null) {
            dto.setApplicationId(agreement.getApplication().getId());
        }
        dto.setOwner(toUserResponse(agreement.getOwner()));
        dto.setTenant(toUserResponse(agreement.getTenant()));
        dto.setProperty(toPropertyResponse(agreement.getProperty()));
        dto.setStartDate(agreement.getStartDate());
        dto.setEndDate(agreement.getEndDate());
        dto.setMonthlyRent(agreement.getMonthlyRent());
        dto.setSecurityDeposit(agreement.getSecurityDeposit());
        dto.setNoticePeriodDays(agreement.getNoticePeriodDays());
        dto.setAgreementStatus(agreement.getAgreementStatus());
        dto.setCreatedAt(agreement.getCreatedAt());
        return dto;
    }

    public static RentRecordResponse toRentRecordResponse(RentRecord record) {
        if (record == null) return null;
        RentRecordResponse dto = new RentRecordResponse();
        dto.setId(record.getId());
        if (record.getAgreement() != null) {
            dto.setAgreementId(record.getAgreement().getId());
        }
        dto.setTenant(toUserResponse(record.getTenant()));
        dto.setProperty(toPropertyResponse(record.getProperty()));
        dto.setMonthYear(record.getMonthYear());
        dto.setDueDate(record.getDueDate());
        dto.setAmount(record.getAmount());
        dto.setStatus(record.getStatus());
        dto.setPaidDate(record.getPaidDate());
        dto.setRemarks(record.getRemarks());
        dto.setCreatedAt(record.getCreatedAt());
        return dto;
    }

    public static MaintenanceResponse toMaintenanceResponse(MaintenanceRequest req) {
        if (req == null) return null;
        MaintenanceResponse dto = new MaintenanceResponse();
        dto.setId(req.getId());
        dto.setTenant(toUserResponse(req.getTenant()));
        dto.setOwner(toUserResponse(req.getOwner()));
        dto.setProperty(toPropertyResponse(req.getProperty()));
        dto.setTitle(req.getTitle());
        dto.setDescription(req.getDescription());
        dto.setPriority(req.getPriority());
        dto.setStatus(req.getStatus());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setResolvedAt(req.getResolvedAt());
        return dto;
    }

    public static ComplaintResponse toComplaintResponse(Complaint complaint) {
        if (complaint == null) return null;
        ComplaintResponse dto = new ComplaintResponse();
        dto.setId(complaint.getId());
        dto.setTenant(toUserResponse(complaint.getTenant()));
        dto.setProperty(toPropertyResponse(complaint.getProperty()));
        dto.setOwner(toUserResponse(complaint.getOwner()));
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());
        dto.setAdminResponse(complaint.getAdminResponse());
        dto.setCreatedAt(complaint.getCreatedAt());
        dto.setResolvedAt(complaint.getResolvedAt());
        return dto;
    }

    public static ReviewResponse toReviewResponse(Review review) {
        if (review == null) return null;
        ReviewResponse dto = new ReviewResponse();
        dto.setId(review.getId());
        dto.setTenant(toUserResponse(review.getTenant()));
        dto.setOwner(toUserResponse(review.getOwner()));
        dto.setProperty(toPropertyResponse(review.getProperty()));
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) return null;
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
