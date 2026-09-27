package com.propertyrental.service;

import com.propertyrental.dto.request.MaintenanceRequestCreate;
import com.propertyrental.dto.response.MaintenanceResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.AgreementStatus;
import com.propertyrental.entity.Enums.MaintenanceStatus;
import com.propertyrental.exception.CustomExceptions.BadRequestException;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.exception.CustomExceptions.UnauthorizedException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRequestRepository maintenanceRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalAgreementRepository agreementRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public MaintenanceResponse createRequest(MaintenanceRequestCreate request, String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + request.getPropertyId()));

        // Verification: Tenant must have an active or recently terminated rental agreement for this property
        boolean hasValidAgreement = agreementRepository.findFirstByTenantIdAndPropertyIdAndAgreementStatusIn(
                tenant.getId(), property.getId(), Arrays.asList(AgreementStatus.ACTIVE, AgreementStatus.EXPIRED, AgreementStatus.TERMINATED)
        ).isPresent();

        if (!hasValidAgreement) {
            throw new BadRequestException("You can only submit maintenance requests for properties you have rented.");
        }

        MaintenanceRequest req = new MaintenanceRequest();
        req.setTenant(tenant);
        req.setOwner(property.getOwner());
        req.setProperty(property);
        req.setTitle(request.getTitle());
        req.setDescription(request.getDescription());
        req.setPriority(request.getPriority());
        req.setStatus(MaintenanceStatus.OPEN);

        MaintenanceRequest saved = maintenanceRepository.save(req);

        notificationService.createNotification(
                property.getOwner(),
                "New Maintenance Request Submitted",
                "Tenant " + tenant.getFullName() + " submitted a maintenance request: '" + saved.getTitle() + "' (Priority: " + saved.getPriority() + ").",
                "MAINTENANCE_CREATED"
        );

        return EntityDtoMapper.toMaintenanceResponse(saved);
    }

    public List<MaintenanceResponse> getTenantRequests(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return maintenanceRepository.findByTenantId(tenant.getId()).stream()
                .map(EntityDtoMapper::toMaintenanceResponse)
                .collect(Collectors.toList());
    }

    public List<MaintenanceResponse> getOwnerRequests(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return maintenanceRepository.findByOwnerId(owner.getId()).stream()
                .map(EntityDtoMapper::toMaintenanceResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceResponse updateStatus(Long requestId, MaintenanceStatus status, String ownerEmail) {
        MaintenanceRequest req = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance request not found with ID: " + requestId));

        if (!req.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only update maintenance requests for your owned properties.");
        }

        req.setStatus(status);
        if (status == MaintenanceStatus.RESOLVED) {
            req.setResolvedAt(LocalDateTime.now());
        }

        MaintenanceRequest updated = maintenanceRepository.save(req);

        notificationService.createNotification(
                req.getTenant(),
                "Maintenance Request Status Updated",
                "Your maintenance request '" + req.getTitle() + "' status has been updated to: " + status,
                "MAINTENANCE_STATUS_UPDATED"
        );

        return EntityDtoMapper.toMaintenanceResponse(updated);
    }
}
