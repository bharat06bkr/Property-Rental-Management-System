package com.propertyrental.service;

import com.propertyrental.dto.response.RentRecordResponse;
import com.propertyrental.entity.Enums.RentStatus;
import com.propertyrental.entity.RentRecord;
import com.propertyrental.entity.RentalAgreement;
import com.propertyrental.entity.User;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.exception.CustomExceptions.UnauthorizedException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.RentRecordRepository;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentRecordService {

    @Autowired
    private RentRecordRepository rentRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public RentRecord generateMonthlyRentRecord(RentalAgreement agreement) {
        String monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        return rentRecordRepository.findByAgreementIdAndMonthYear(agreement.getId(), monthYear)
                .orElseGet(() -> {
                    RentRecord record = new RentRecord();
                    record.setAgreement(agreement);
                    record.setTenant(agreement.getTenant());
                    record.setProperty(agreement.getProperty());
                    record.setMonthYear(monthYear);
                    record.setDueDate(LocalDate.now().withDayOfMonth(5)); // Due on 5th of the month
                    record.setAmount(agreement.getMonthlyRent());
                    record.setStatus(RentStatus.PENDING);
                    record.setRemarks("Monthly rent record generated");

                    RentRecord saved = rentRecordRepository.save(record);

                    notificationService.createNotification(
                            agreement.getTenant(),
                            "Monthly Rent Due",
                            "Monthly rent record for " + monthYear + " (₹" + agreement.getMonthlyRent() + ") has been generated. Due date: " + record.getDueDate(),
                            "RENT_DUE"
                    );

                    return saved;
                });
    }

    public List<RentRecordResponse> getTenantRentRecords(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return rentRecordRepository.findByTenantId(tenant.getId()).stream()
                .map(EntityDtoMapper::toRentRecordResponse)
                .collect(Collectors.toList());
    }

    public List<RentRecordResponse> getOwnerRentRecords(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return rentRecordRepository.findByAgreementOwnerId(owner.getId()).stream()
                .map(EntityDtoMapper::toRentRecordResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RentRecordResponse updateRentStatus(Long recordId, RentStatus newStatus, String remarks, String ownerEmail) {
        RentRecord record = rentRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Rent record not found with ID: " + recordId));

        if (!record.getAgreement().getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only update rent records for your owned properties.");
        }

        record.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            record.setRemarks(remarks);
        }

        if (newStatus == RentStatus.PAID) {
            record.setPaidDate(LocalDate.now());
        }

        RentRecord updated = rentRecordRepository.save(record);

        notificationService.createNotification(
                record.getTenant(),
                "Rent Payment Status Updated",
                "Your rent payment status for " + record.getMonthYear() + " has been updated to: " + newStatus,
                "RENT_STATUS_UPDATED"
        );

        return EntityDtoMapper.toRentRecordResponse(updated);
    }
}
