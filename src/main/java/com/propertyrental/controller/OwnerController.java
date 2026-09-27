package com.propertyrental.controller;

import com.propertyrental.dto.request.PropertyRequest;
import com.propertyrental.dto.response.*;
import com.propertyrental.entity.Enums.MaintenanceStatus;
import com.propertyrental.entity.Enums.RentStatus;
import com.propertyrental.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@PreAuthorize("hasRole('OWNER')")
public class OwnerController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private RentRecordService rentRecordService;

    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping("/properties")
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        PropertyResponse response = propertyService.createProperty(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Property listing submitted for review", response));
    }

    @GetMapping("/properties")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getOwnerProperties(Authentication authentication) {
        List<PropertyResponse> response = propertyService.getOwnerProperties(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Owner properties retrieved", response));
    }

    @PutMapping("/properties/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        PropertyResponse response = propertyService.updateProperty(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Property updated successfully", response));
    }

    @DeleteMapping("/properties/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateProperty(@PathVariable Long id, Authentication authentication) {
        propertyService.deactivateProperty(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Property deactivated successfully", "INACTIVE"));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getOwnerApplications(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Rental applications retrieved", applicationService.getOwnerApplications(authentication.getName())));
    }

    @PutMapping("/applications/{id}/accept")
    public ResponseEntity<ApiResponse<ApplicationResponse>> acceptApplication(@PathVariable Long id, Authentication authentication) {
        ApplicationResponse response = applicationService.acceptApplication(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Application accepted and rental agreement activated", response));
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<ApiResponse<ApplicationResponse>> rejectApplication(@PathVariable Long id, Authentication authentication) {
        ApplicationResponse response = applicationService.rejectApplication(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Application rejected", response));
    }

    @GetMapping("/agreements")
    public ResponseEntity<ApiResponse<List<AgreementResponse>>> getOwnerAgreements(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Rental agreements retrieved", agreementService.getOwnerAgreements(authentication.getName())));
    }

    @GetMapping("/rent-records")
    public ResponseEntity<ApiResponse<List<RentRecordResponse>>> getOwnerRentRecords(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Rent records retrieved", rentRecordService.getOwnerRentRecords(authentication.getName())));
    }

    @PutMapping("/rent-records/{id}/status")
    public ResponseEntity<ApiResponse<RentRecordResponse>> updateRentStatus(
            @PathVariable Long id,
            @RequestParam RentStatus status,
            @RequestParam(required = false) String remarks,
            Authentication authentication
    ) {
        RentRecordResponse response = rentRecordService.updateRentStatus(id, status, remarks, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Rent status updated", response));
    }

    @GetMapping("/maintenance")
    public ResponseEntity<ApiResponse<List<MaintenanceResponse>>> getOwnerMaintenanceRequests(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Maintenance requests retrieved", maintenanceService.getOwnerRequests(authentication.getName())));
    }

    @PutMapping("/maintenance/{id}/status")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> updateMaintenanceStatus(
            @PathVariable Long id,
            @RequestParam MaintenanceStatus status,
            Authentication authentication
    ) {
        MaintenanceResponse response = maintenanceService.updateStatus(id, status, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Maintenance status updated", response));
    }
}
