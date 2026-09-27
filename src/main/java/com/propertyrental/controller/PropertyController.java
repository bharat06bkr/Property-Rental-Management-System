package com.propertyrental.controller;

import com.propertyrental.dto.response.*;
import com.propertyrental.entity.Amenity;
import com.propertyrental.entity.PropertyType;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.AmenityRepository;
import com.propertyrental.repository.PropertyTypeRepository;
import com.propertyrental.service.PropertyService;
import com.propertyrental.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) Long propertyTypeId,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) String furnishingStatus
    ) {
        List<PropertyResponse> properties = propertyService.searchAvailableProperties(
                city, area, propertyTypeId, minRent, maxRent, bedrooms, bathrooms, furnishingStatus
        );
        return ResponseEntity.ok(ApiResponse.success("Properties matching search criteria", properties));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@PathVariable Long id) {
        PropertyResponse property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(ApiResponse.success("Property details retrieved", property));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<PropertyTypeResponse>>> getPropertyTypes() {
        List<PropertyTypeResponse> types = propertyTypeRepository.findAll().stream()
                .map(EntityDtoMapper::toPropertyTypeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Property types retrieved", types));
    }

    @GetMapping("/amenities")
    public ResponseEntity<ApiResponse<List<AmenityResponse>>> getAmenities() {
        List<AmenityResponse> amenities = amenityRepository.findAll().stream()
                .map(EntityDtoMapper::toAmenityResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Amenities catalog retrieved", amenities));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getPropertyReviews(@PathVariable Long id) {
        List<ReviewResponse> reviews = reviewService.getPropertyReviews(id);
        return ResponseEntity.ok(ApiResponse.success("Property reviews retrieved", reviews));
    }
}
