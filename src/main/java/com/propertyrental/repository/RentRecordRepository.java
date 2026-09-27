package com.propertyrental.repository;

import com.propertyrental.entity.Enums.RentStatus;
import com.propertyrental.entity.RentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentRecordRepository extends JpaRepository<RentRecord, Long> {

    List<RentRecord> findByAgreementId(Long agreementId);

    List<RentRecord> findByTenantId(Long tenantId);

    List<RentRecord> findByAgreementOwnerId(Long ownerId);

    Optional<RentRecord> findByAgreementIdAndMonthYear(Long agreementId, String monthYear);

    List<RentRecord> findByStatus(RentStatus status);
}
