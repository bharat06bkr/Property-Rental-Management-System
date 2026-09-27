package com.propertyrental.repository;

import com.propertyrental.entity.Enums.ApprovalStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import com.propertyrental.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);

    List<Property> findByApprovalStatus(ApprovalStatus approvalStatus);

    List<Property> findByApprovalStatusAndAvailabilityStatus(ApprovalStatus approvalStatus, AvailabilityStatus availabilityStatus);

    @Query("SELECT DISTINCT p FROM Property p " +
           "JOIN p.location l " +
           "JOIN p.propertyType pt " +
           "WHERE p.approvalStatus = :approvalStatus " +
           "AND p.availabilityStatus = :availabilityStatus " +
           "AND (:city IS NULL OR LOWER(l.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:area IS NULL OR LOWER(l.area) LIKE LOWER(CONCAT('%', :area, '%'))) " +
           "AND (:propertyTypeId IS NULL OR pt.id = :propertyTypeId) " +
           "AND (:minRent IS NULL OR p.monthlyRent >= :minRent) " +
           "AND (:maxRent IS NULL OR p.monthlyRent <= :maxRent) " +
           "AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms) " +
           "AND (:bathrooms IS NULL OR p.bathrooms = :bathrooms) " +
           "AND (:furnishingStatus IS NULL OR LOWER(p.furnishingStatus) = LOWER(:furnishingStatus))")
    List<Property> searchProperties(
            @Param("approvalStatus") ApprovalStatus approvalStatus,
            @Param("availabilityStatus") AvailabilityStatus availabilityStatus,
            @Param("city") String city,
            @Param("area") String area,
            @Param("propertyTypeId") Long propertyTypeId,
            @Param("minRent") BigDecimal minRent,
            @Param("maxRent") BigDecimal maxRent,
            @Param("bedrooms") Integer bedrooms,
            @Param("bathrooms") Integer bathrooms,
            @Param("furnishingStatus") String furnishingStatus
    );
}
