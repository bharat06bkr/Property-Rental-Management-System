package com.propertyrental.repository;

import com.propertyrental.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByTenantId(Long tenantId);

    List<Complaint> findByOwnerId(Long ownerId);

    List<Complaint> findByPropertyId(Long propertyId);
}
