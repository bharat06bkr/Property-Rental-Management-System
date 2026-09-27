package com.propertyrental.entity;

public class Enums {

    public enum Role {
        ADMIN,
        OWNER,
        TENANT
    }

    public enum OwnerStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum AvailabilityStatus {
        AVAILABLE,
        RENTED,
        MAINTENANCE,
        INACTIVE
    }

    public enum ApplicationStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED
    }

    public enum AgreementStatus {
        ACTIVE,
        EXPIRED,
        TERMINATED,
        CANCELLED
    }

    public enum RentStatus {
        PENDING,
        PAID,
        OVERDUE,
        WAIVED
    }

    public enum MaintenancePriority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    public enum MaintenanceStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CANCELLED
    }

    public enum ComplaintStatus {
        OPEN,
        UNDER_REVIEW,
        RESOLVED,
        REJECTED,
        CLOSED
    }
}
