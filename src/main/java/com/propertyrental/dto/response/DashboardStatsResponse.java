package com.propertyrental.dto.response;

public class DashboardStatsResponse {

    private long totalUsers;
    private long totalOwners;
    private long totalTenants;
    private long pendingOwners;

    private long totalProperties;
    private long pendingProperties;
    private long approvedProperties;
    private long rentedProperties;

    private long pendingApplications;
    private long activeAgreements;
    private long openMaintenanceRequests;
    private long openComplaints;

    public DashboardStatsResponse() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalOwners() { return totalOwners; }
    public void setTotalOwners(long totalOwners) { this.totalOwners = totalOwners; }

    public long getTotalTenants() { return totalTenants; }
    public void setTotalTenants(long totalTenants) { this.totalTenants = totalTenants; }

    public long getPendingOwners() { return pendingOwners; }
    public void setPendingOwners(long pendingOwners) { this.pendingOwners = pendingOwners; }

    public long getTotalProperties() { return totalProperties; }
    public void setTotalProperties(long totalProperties) { this.totalProperties = totalProperties; }

    public long getPendingProperties() { return pendingProperties; }
    public void setPendingProperties(long pendingProperties) { this.pendingProperties = pendingProperties; }

    public long getApprovedProperties() { return approvedProperties; }
    public void setApprovedProperties(long approvedProperties) { this.approvedProperties = approvedProperties; }

    public long getRentedProperties() { return rentedProperties; }
    public void setRentedProperties(long rentedProperties) { this.rentedProperties = rentedProperties; }

    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long pendingApplications) { this.pendingApplications = pendingApplications; }

    public long getActiveAgreements() { return activeAgreements; }
    public void setActiveAgreements(long activeAgreements) { this.activeAgreements = activeAgreements; }

    public long getOpenMaintenanceRequests() { return openMaintenanceRequests; }
    public void setOpenMaintenanceRequests(long openMaintenanceRequests) { this.openMaintenanceRequests = openMaintenanceRequests; }

    public long getOpenComplaints() { return openComplaints; }
    public void setOpenComplaints(long openComplaints) { this.openComplaints = openComplaints; }
}
