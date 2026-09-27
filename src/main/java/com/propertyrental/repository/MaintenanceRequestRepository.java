package com.propertyrental.repository;

import com.propertyrental.entity.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

    List<MaintenanceRequest> findByTenantId(Long tenantId);

    List<MaintenanceRequest> findByOwnerId(Long ownerId);

    List<MaintenanceRequest> findByPropertyId(Long propertyId);
}
