package com.propertyrental.dto.response;

import com.propertyrental.entity.Enums.AgreementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgreementResponse {

    private Long id;
    private Long applicationId;
    private UserResponse owner;
    private UserResponse tenant;
    private PropertyResponse property;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private Integer noticePeriodDays;

    private AgreementStatus agreementStatus;
    private LocalDateTime createdAt;

    public AgreementResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public UserResponse getTenant() { return tenant; }
    public void setTenant(UserResponse tenant) { this.tenant = tenant; }

    public PropertyResponse getProperty() { return property; }
    public void setProperty(PropertyResponse property) { this.property = property; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }

    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }

    public Integer getNoticePeriodDays() { return noticePeriodDays; }
    public void setNoticePeriodDays(Integer noticePeriodDays) { this.noticePeriodDays = noticePeriodDays; }

    public AgreementStatus getAgreementStatus() { return agreementStatus; }
    public void setAgreementStatus(AgreementStatus agreementStatus) { this.agreementStatus = agreementStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
