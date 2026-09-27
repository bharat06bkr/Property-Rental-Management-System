package com.propertyrental.repository;

import com.propertyrental.entity.Enums.AgreementStatus;
import com.propertyrental.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {

    List<RentalAgreement> findByTenantId(Long tenantId);

    List<RentalAgreement> findByOwnerId(Long ownerId);

    List<RentalAgreement> findByPropertyId(Long propertyId);

    Optional<RentalAgreement> findByPropertyIdAndAgreementStatus(Long propertyId, AgreementStatus status);

    Optional<RentalAgreement> findByApplicationId(Long applicationId);

    Optional<RentalAgreement> findFirstByTenantIdAndPropertyIdAndAgreementStatusIn(
            Long tenantId, Long propertyId, List<AgreementStatus> statuses
    );

    List<RentalAgreement> findByAgreementStatus(AgreementStatus status);
}
