package com.propertyrental.service;

import com.propertyrental.dto.request.ReviewRequest;
import com.propertyrental.dto.response.ReviewResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.AgreementStatus;
import com.propertyrental.exception.CustomExceptions.BadRequestException;
import com.propertyrental.exception.CustomExceptions.DuplicateResourceException;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalAgreementRepository agreementRepository;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request, String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + request.getPropertyId()));

        // Business Rule #22: Tenant can review ONLY a property they have actually rented (valid completed/past or active agreement)
        boolean hasAgreement = agreementRepository.findFirstByTenantIdAndPropertyIdAndAgreementStatusIn(
                tenant.getId(), property.getId(), Arrays.asList(AgreementStatus.ACTIVE, AgreementStatus.EXPIRED, AgreementStatus.TERMINATED)
        ).isPresent();

        if (!hasAgreement) {
            throw new BadRequestException("You can only submit a review for a property after having an established rental agreement with it.");
        }

        // Prevent duplicate reviews
        if (reviewRepository.findByTenantIdAndPropertyId(tenant.getId(), property.getId()).isPresent()) {
            throw new DuplicateResourceException("You have already submitted a review for this property.");
        }

        Review review = new Review();
        review.setTenant(tenant);
        review.setOwner(property.getOwner());
        review.setProperty(property);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return EntityDtoMapper.toReviewResponse(saved);
    }

    public List<ReviewResponse> getPropertyReviews(Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId).stream()
                .map(EntityDtoMapper::toReviewResponse)
                .collect(Collectors.toList());
    }
}
