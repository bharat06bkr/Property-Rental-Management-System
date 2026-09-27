package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.RentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentRecordResponse {

    private Long id;
    private Long agreementId;
    private UserResponse tenant;
    private PropertyResponse property;

    private String monthYear;
    private LocalDate dueDate;
    private BigDecimal amount;
    private RentStatus status;
    private LocalDate paidDate;
    private String remarks;

    private LocalDateTime createdAt;

    public RentRecordResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAgreementId() { return agreementId; }
    public void setAgreementId(Long agreementId) { this.agreementId = agreementId; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public RentStatus getStatus() { return status; }
    public void setStatus(RentStatus status) { this.status = status; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
