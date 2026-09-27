package com.propertyrental.controller;

import com.propertyrental.dto.request.ApprovalDecisionRequest;
import com.propertyrental.dto.response.*;
import com.propertyrental.entity.Enums.ComplaintStatus;
import com.propertyrental.entity.Enums.OwnerStatus;
import com.propertyrental.service.AdminService;
import com.propertyrental.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        DashboardStatsResponse stats = adminService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success("Dashboard statistics fetched", stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("All users retrieved", adminService.getAllUsers()));
    }

    @GetMapping("/owners")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getOwners(@RequestParam(required = false) OwnerStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Owners retrieved", adminService.getOwnersByStatus(status)));
    }

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getTenants() {
        return ResponseEntity.ok(ApiResponse.success("Tenants retrieved", adminService.getAllTenants()));
    }

    @PutMapping("/owners/{id}/approve")
    public ResponseEntity<ApiResponse<UserResponse>> approveOrRejectOwner(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalDecisionRequest request
    ) {
        UserResponse response = adminService.approveOrRejectOwner(id, request);
        return ResponseEntity.ok(ApiResponse.success("Owner status updated", response));
    }

    @GetMapping("/properties/pending")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getPendingProperties() {
        return ResponseEntity.ok(ApiResponse.success("Pending properties retrieved", adminService.getPendingProperties()));
    }

    @PutMapping("/properties/{id}/approve")
    public ResponseEntity<ApiResponse<PropertyResponse>> approveOrRejectProperty(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalDecisionRequest request
    ) {
        PropertyResponse response = adminService.approveOrRejectProperty(id, request);
        return ResponseEntity.ok(ApiResponse.success("Property approval status updated", response));
    }

    @GetMapping("/complaints")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getAllComplaints() {
        return ResponseEntity.ok(ApiResponse.success("All complaints retrieved", complaintService.getAllComplaintsForAdmin()));
    }

    @PutMapping("/complaints/{id}/respond")
    public ResponseEntity<ApiResponse<ComplaintResponse>> resolveComplaint(
            @PathVariable Long id,
            @RequestParam ComplaintStatus status,
            @RequestBody(required = false) String adminResponse
    ) {
        ComplaintResponse response = complaintService.resolveComplaintByAdmin(id, status, adminResponse);
        return ResponseEntity.ok(ApiResponse.success("Complaint updated", response));
    }
}
