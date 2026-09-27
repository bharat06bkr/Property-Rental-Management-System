package com.propertyrental.controller;

import com.propertyrental.dto.request.*;
import com.propertyrental.dto.response.*;
import com.propertyrental.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
@PreAuthorize("hasRole('TENANT')")
public class TenantController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private RentRecordService rentRecordService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<ApplicationResponse>> submitApplication(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication
    ) {
        ApplicationResponse response = applicationService.submitApplication(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Rental application submitted successfully", response));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getTenantApplications(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved", applicationService.getTenantApplications(authentication.getName())));
    }

    @PutMapping("/applications/{id}/cancel")
    public ResponseEntity<ApiResponse<ApplicationResponse>> cancelApplication(@PathVariable Long id, Authentication authentication) {
        ApplicationResponse response = applicationService.cancelApplication(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Application cancelled", response));
    }

    @GetMapping("/agreements")
    public ResponseEntity<ApiResponse<List<AgreementResponse>>> getTenantAgreements(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Rental agreements retrieved", agreementService.getTenantAgreements(authentication.getName())));
    }

    @PutMapping("/agreements/{id}/terminate")
    public ResponseEntity<ApiResponse<AgreementResponse>> terminateAgreement(@PathVariable Long id, Authentication authentication) {
        AgreementResponse response = agreementService.terminateAgreement(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Rental agreement terminated. Property is now available.", response));
    }

    @GetMapping("/rent-records")
    public ResponseEntity<ApiResponse<List<RentRecordResponse>>> getTenantRentRecords(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Rent records retrieved", rentRecordService.getTenantRentRecords(authentication.getName())));
    }

    @PostMapping("/maintenance")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> createMaintenanceRequest(
            @Valid @RequestBody MaintenanceRequestCreate request,
            Authentication authentication
    ) {
        MaintenanceResponse response = maintenanceService.createRequest(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Maintenance request submitted", response));
    }

    @GetMapping("/maintenance")
    public ResponseEntity<ApiResponse<List<MaintenanceResponse>>> getTenantMaintenanceRequests(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Maintenance requests retrieved", maintenanceService.getTenantRequests(authentication.getName())));
    }

    @PostMapping("/complaints")
    public ResponseEntity<ApiResponse<ComplaintResponse>> createComplaint(
            @Valid @RequestBody ComplaintCreateRequest request,
            Authentication authentication
    ) {
        ComplaintResponse response = complaintService.createComplaint(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Complaint submitted to Admin", response));
    }

    @GetMapping("/complaints")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getTenantComplaints(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved", complaintService.getTenantComplaints(authentication.getName())));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        ReviewResponse response = reviewService.createReview(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Review posted successfully", response));
    }
}
