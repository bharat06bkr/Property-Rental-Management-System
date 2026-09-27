package com.propertyrental.service;

import com.propertyrental.dto.response.AgreementResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.AgreementStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.exception.CustomExceptions.UnauthorizedException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.PropertyRepository;
import com.propertyrental.repository.RentalAgreementRepository;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgreementService {

    @Autowired
    private RentalAgreementRepository agreementRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRecordService rentRecordService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public RentalAgreement createAgreementFromApplication(RentalApplication application) {
        RentalAgreement agreement = new RentalAgreement();
        agreement.setApplication(application);
        agreement.setOwner(application.getProperty().getOwner());
        agreement.setTenant(application.getTenant());
        agreement.setProperty(application.getProperty());

        LocalDate startDate = application.getPreferredMoveInDate() != null ? application.getPreferredMoveInDate() : LocalDate.now();
        agreement.setStartDate(startDate);
        agreement.setEndDate(startDate.plusMonths(11));
        agreement.setMonthlyRent(application.getProperty().getMonthlyRent());
        agreement.setSecurityDeposit(application.getProperty().getSecurityDeposit());
        agreement.setNoticePeriodDays(30);
        agreement.setAgreementStatus(AgreementStatus.ACTIVE);

        RentalAgreement savedAgreement = agreementRepository.save(agreement);

        // Automatically generate initial monthly rent record
        rentRecordService.generateMonthlyRentRecord(savedAgreement);

        return savedAgreement;
    }

    public List<AgreementResponse> getTenantAgreements(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return agreementRepository.findByTenantId(tenant.getId()).stream()
                .map(EntityDtoMapper::toAgreementResponse)
                .collect(Collectors.toList());
    }

    public List<AgreementResponse> getOwnerAgreements(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return agreementRepository.findByOwnerId(owner.getId()).stream()
                .map(EntityDtoMapper::toAgreementResponse)
                .collect(Collectors.toList());
    }

    public AgreementResponse getAgreementById(Long id, String email) {
        RentalAgreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental Agreement not found with ID: " + id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isOwner = agreement.getOwner().getId().equals(user.getId());
        boolean isTenant = agreement.getTenant().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Enums.Role.ADMIN;

        if (!isOwner && !isTenant && !isAdmin) {
            throw new UnauthorizedException("You are not authorized to view this agreement.");
        }

        return EntityDtoMapper.toAgreementResponse(agreement);
    }

    @Transactional
    public AgreementResponse terminateAgreement(Long agreementId, String userEmail) {
        RentalAgreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental agreement not found with ID: " + agreementId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isOwner = agreement.getOwner().getId().equals(user.getId());
        boolean isTenant = agreement.getTenant().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Enums.Role.ADMIN;

        if (!isOwner && !isTenant && !isAdmin) {
            throw new UnauthorizedException("You do not have permission to terminate this rental agreement.");
        }

        agreement.setAgreementStatus(AgreementStatus.TERMINATED);
        RentalAgreement savedAgreement = agreementRepository.save(agreement);

        // Crucial Business Rule: Mark property back as AVAILABLE
        Property property = agreement.getProperty();
        property.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        propertyRepository.save(property);

        notificationService.createNotification(
                agreement.getTenant(),
                "Rental Agreement Terminated",
                "Your rental agreement for '" + property.getTitle() + "' has been terminated. You can now leave a property review.",
                "AGREEMENT_TERMINATED"
        );

        notificationService.createNotification(
                agreement.getOwner(),
                "Rental Agreement Terminated",
                "The rental agreement for '" + property.getTitle() + "' was terminated. Property is marked AVAILABLE.",
                "AGREEMENT_TERMINATED"
        );

        return EntityDtoMapper.toAgreementResponse(savedAgreement);
    }
}
