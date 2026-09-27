package com.propertyrental.service;

import com.propertyrental.dto.request.ApprovalDecisionRequest;
import com.propertyrental.dto.response.*;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.*;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private RentalApplicationRepository applicationRepository;

    @Autowired
    private RentalAgreementRepository agreementRepository;

    @Autowired
    private MaintenanceRequestRepository maintenanceRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private NotificationService notificationService;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityDtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getOwnersByStatus(OwnerStatus status) {
        List<User> owners = status != null ?
                userRepository.findByRoleAndOwnerStatus(Role.OWNER, status) :
                userRepository.findByRole(Role.OWNER);

        return owners.stream()
                .map(EntityDtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllTenants() {
        return userRepository.findByRole(Role.TENANT).stream()
                .map(EntityDtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse approveOrRejectOwner(Long ownerId, ApprovalDecisionRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with ID: " + ownerId));

        if (owner.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Target user is not a property owner");
        }

        if (request.getApproved()) {
            owner.setOwnerStatus(OwnerStatus.APPROVED);
            notificationService.createNotification(
                    owner,
                    "Owner Registration Approved",
                    "Congratulations! Your owner account has been approved by admin. You can now list properties.",
                    "OWNER_APPROVED"
            );
        } else {
            owner.setOwnerStatus(OwnerStatus.REJECTED);
            notificationService.createNotification(
                    owner,
                    "Owner Registration Rejected",
                    "Your owner application was rejected. Reason: " + (request.getRejectionReason() != null ? request.getRejectionReason() : "Criteria not met"),
                    "OWNER_REJECTED"
            );
        }

        return EntityDtoMapper.toUserResponse(userRepository.save(owner));
    }

    public List<PropertyResponse> getPendingProperties() {
        return propertyRepository.findByApprovalStatus(ApprovalStatus.PENDING).stream()
                .map(EntityDtoMapper::toPropertyResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PropertyResponse approveOrRejectProperty(Long propertyId, ApprovalDecisionRequest request) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));

        if (request.getApproved()) {
            property.setApprovalStatus(ApprovalStatus.APPROVED);
            property.setRejectionReason(null);
            notificationService.createNotification(
                    property.getOwner(),
                    "Property Listing Approved",
                    "Your property '" + property.getTitle() + "' was approved and is now visible to tenants.",
                    "PROPERTY_APPROVED"
            );
        } else {
            property.setApprovalStatus(ApprovalStatus.REJECTED);
            property.setRejectionReason(request.getRejectionReason() != null ? request.getRejectionReason() : "Admin rejected listing");
            notificationService.createNotification(
                    property.getOwner(),
                    "Property Listing Rejected",
                    "Your property '" + property.getTitle() + "' was rejected. Reason: " + property.getRejectionReason(),
                    "PROPERTY_REJECTED"
            );
        }

        return EntityDtoMapper.toPropertyResponse(propertyRepository.save(property));
    }

    public DashboardStatsResponse getDashboardStatistics() {
        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalOwners(userRepository.findByRole(Role.OWNER).size());
        stats.setTotalTenants(userRepository.findByRole(Role.TENANT).size());
        stats.setPendingOwners(userRepository.findByOwnerStatus(OwnerStatus.PENDING).size());

        stats.setTotalProperties(propertyRepository.count());
        stats.setPendingProperties(propertyRepository.findByApprovalStatus(ApprovalStatus.PENDING).size());
        stats.setApprovedProperties(propertyRepository.findByApprovalStatus(ApprovalStatus.APPROVED).size());
        stats.setRentedProperties(propertyRepository.findByApprovalStatusAndAvailabilityStatus(ApprovalStatus.APPROVED, AvailabilityStatus.RENTED).size());

        stats.setPendingApplications(applicationRepository.findAll().stream().filter(a -> a.getStatus() == ApplicationStatus.PENDING).count());
        stats.setActiveAgreements(agreementRepository.findByAgreementStatus(AgreementStatus.ACTIVE).size());
        stats.setOpenMaintenanceRequests(maintenanceRepository.findAll().stream().filter(m -> m.getStatus() == MaintenanceStatus.OPEN || m.getStatus() == MaintenanceStatus.IN_PROGRESS).count());
        stats.setOpenComplaints(complaintRepository.findAll().stream().filter(c -> c.getStatus() == ComplaintStatus.OPEN || c.getStatus() == ComplaintStatus.UNDER_REVIEW).count());

        return stats;
    }
}
