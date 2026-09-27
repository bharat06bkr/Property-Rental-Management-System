package com.propertyrental.repository;

import com.propertyrental.entity.Enums.ApplicationStatus;
import com.propertyrental.entity.RentalApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {

    List<RentalApplication> findByTenantId(Long tenantId);

    List<RentalApplication> findByPropertyOwnerId(Long ownerId);

    List<RentalApplication> findByPropertyId(Long propertyId);

    Optional<RentalApplication> findByTenantIdAndPropertyIdAndStatusIn(Long tenantId, Long propertyId, List<ApplicationStatus> statuses);
}
