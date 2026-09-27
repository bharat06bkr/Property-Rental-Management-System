package com.propertyrental.service;

import com.propertyrental.dto.request.ComplaintCreateRequest;
import com.propertyrental.dto.response.ComplaintResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.ComplaintStatus;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ComplaintResponse createComplaint(ComplaintCreateRequest request, String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        Property property = null;
        if (request.getPropertyId() != null) {
            property = propertyRepository.findById(request.getPropertyId()).orElse(null);
        }

        User owner = null;
        if (request.getOwnerId() != null) {
            owner = userRepository.findById(request.getOwnerId()).orElse(null);
        } else if (property != null) {
            owner = property.getOwner();
        }

        Complaint complaint = new Complaint();
        complaint.setTenant(tenant);
        complaint.setProperty(property);
        complaint.setOwner(owner);
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setStatus(ComplaintStatus.OPEN);

        Complaint saved = complaintRepository.save(complaint);

        return EntityDtoMapper.toComplaintResponse(saved);
    }

    public List<ComplaintResponse> getTenantComplaints(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return complaintRepository.findByTenantId(tenant.getId()).stream()
                .map(EntityDtoMapper::toComplaintResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getAllComplaintsForAdmin() {
        return complaintRepository.findAll().stream()
                .map(EntityDtoMapper::toComplaintResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintResponse resolveComplaintByAdmin(Long complaintId, ComplaintStatus status, String adminResponse) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + complaintId));

        complaint.setStatus(status);
        if (adminResponse != null) {
            complaint.setAdminResponse(adminResponse);
        }

        if (status == ComplaintStatus.RESOLVED || status == ComplaintStatus.CLOSED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }

        Complaint updated = complaintRepository.save(complaint);

        notificationService.createNotification(
                complaint.getTenant(),
                "Complaint Status Updated",
                "Your complaint '" + complaint.getTitle() + "' has been updated by Admin. Status: " + status,
                "COMPLAINT_UPDATED"
        );

        return EntityDtoMapper.toComplaintResponse(updated);
    }
}
