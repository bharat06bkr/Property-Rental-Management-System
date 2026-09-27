package com.propertyrental.service;

import com.propertyrental.dto.request.ApplicationRequest;
import com.propertyrental.dto.response.ApplicationResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.ApplicationStatus;
import com.propertyrental.entity.Enums.ApprovalStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import com.propertyrental.exception.CustomExceptions.BadRequestException;
import com.propertyrental.exception.CustomExceptions.DuplicateResourceException;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.exception.CustomExceptions.UnauthorizedException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.PropertyRepository;
import com.propertyrental.repository.RentalApplicationRepository;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private RentalApplicationRepository applicationRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private AgreementService agreementService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ApplicationResponse submitApplication(ApplicationRequest request, String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant user not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + request.getPropertyId()));

        if (property.getApprovalStatus() != ApprovalStatus.APPROVED || property.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("This property is currently not available for rental applications.");
        }

        // Prevent tenant from submitting duplicate active application for same property
        boolean hasExisting = applicationRepository.findByTenantIdAndPropertyIdAndStatusIn(
                tenant.getId(), property.getId(), Arrays.asList(ApplicationStatus.PENDING, ApplicationStatus.ACCEPTED)
        ).isPresent();

        if (hasExisting) {
            throw new DuplicateResourceException("You already have an active application for this property.");
        }

        RentalApplication application = new RentalApplication();
        application.setTenant(tenant);
        application.setProperty(property);
        application.setApplicationDate(LocalDate.now());
        application.setPreferredMoveInDate(request.getPreferredMoveInDate() != null ? request.getPreferredMoveInDate() : LocalDate.now().plusDays(7));
        application.setMessage(request.getMessage());
        application.setStatus(ApplicationStatus.PENDING);

        RentalApplication saved = applicationRepository.save(application);

        notificationService.createNotification(
                property.getOwner(),
                "New Rental Application Received",
                "Tenant " + tenant.getFullName() + " submitted a rental application for '" + property.getTitle() + "'.",
                "APPLICATION_RECEIVED"
        );

        return EntityDtoMapper.toApplicationResponse(saved);
    }

    public List<ApplicationResponse> getTenantApplications(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return applicationRepository.findByTenantId(tenant.getId()).stream()
                .map(EntityDtoMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getOwnerApplications(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return applicationRepository.findByPropertyOwnerId(owner.getId()).stream()
                .map(EntityDtoMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse acceptApplication(Long applicationId, String ownerEmail) {
        RentalApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        Property property = application.getProperty();

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only accept applications for your own properties.");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BadRequestException("Application cannot be accepted because it is currently: " + application.getStatus());
        }

        if (property.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("Property is no longer available for rental.");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        RentalApplication savedApp = applicationRepository.save(application);

        // Update Property Status to RENTED
        property.setAvailabilityStatus(AvailabilityStatus.RENTED);
        propertyRepository.save(property);

        // Reject other pending applications for this property
        List<RentalApplication> otherPendingApps = applicationRepository.findByPropertyId(property.getId()).stream()
                .filter(a -> !a.getId().equals(applicationId) && a.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());

        for (RentalApplication otherApp : otherPendingApps) {
            otherApp.setStatus(ApplicationStatus.REJECTED);
            applicationRepository.save(otherApp);
            notificationService.createNotification(
                    otherApp.getTenant(),
                    "Application Update",
                    "Your application for '" + property.getTitle() + "' was not selected as another application was accepted.",
                    "APPLICATION_REJECTED"
            );
        }

        // Auto-generate Active Rental Agreement
        agreementService.createAgreementFromApplication(savedApp);

        notificationService.createNotification(
                application.getTenant(),
                "Rental Application Accepted!",
                "Great news! Your application for '" + property.getTitle() + "' has been accepted by the owner. Your rental agreement is active.",
                "APPLICATION_ACCEPTED"
        );

        return EntityDtoMapper.toApplicationResponse(savedApp);
    }

    @Transactional
    public ApplicationResponse rejectApplication(Long applicationId, String ownerEmail) {
        RentalApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        if (!application.getProperty().getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only respond to applications for your own properties.");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BadRequestException("Application is not in PENDING state.");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        RentalApplication savedApp = applicationRepository.save(application);

        notificationService.createNotification(
                application.getTenant(),
                "Rental Application Decision",
                "Your application for '" + application.getProperty().getTitle() + "' was rejected by the owner.",
                "APPLICATION_REJECTED"
        );

        return EntityDtoMapper.toApplicationResponse(savedApp);
    }

    @Transactional
    public ApplicationResponse cancelApplication(Long applicationId, String tenantEmail) {
        RentalApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        if (!application.getTenant().getEmail().equals(tenantEmail)) {
            throw new UnauthorizedException("You can only cancel your own applications.");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BadRequestException("Only PENDING applications can be cancelled.");
        }

        application.setStatus(ApplicationStatus.CANCELLED);
        return EntityDtoMapper.toApplicationResponse(applicationRepository.save(application));
    }
}
