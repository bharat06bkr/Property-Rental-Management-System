package com.propertyrental.repository;

import com.propertyrental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPropertyId(Long propertyId);

    List<Review> findByOwnerId(Long ownerId);

    Optional<Review> findByTenantIdAndPropertyId(Long tenantId, Long propertyId);
}
