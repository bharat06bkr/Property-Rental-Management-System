package com.propertyrental.dto.request;

import jakarta.validation.constraints.NotNull;

public class ApprovalDecisionRequest {

    @NotNull(message = "Decision (approved=true/false) is required")
    private Boolean approved;

    private String rejectionReason;

    public ApprovalDecisionRequest() {}

    public ApprovalDecisionRequest(Boolean approved, String rejectionReason) {
        this.approved = approved;
        this.rejectionReason = rejectionReason;
    }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
