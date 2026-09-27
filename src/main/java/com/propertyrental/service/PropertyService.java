package com.propertyrental.service;

import com.propertyrental.dto.request.PropertyRequest;
import com.propertyrental.dto.response.PropertyResponse;
import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.ApprovalStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import com.propertyrental.entity.Enums.OwnerStatus;
import com.propertyrental.exception.CustomExceptions.BadRequestException;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.exception.CustomExceptions.UnauthorizedException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner user not found"));

        if (owner.getOwnerStatus() != OwnerStatus.APPROVED) {
            throw new BadRequestException("Only approved property owners can list new properties. Current status: " + owner.getOwnerStatus());
        }

        Location location = locationRepository.findByCityIgnoreCaseAndAreaIgnoreCase(request.getCity(), request.getArea())
                .orElseGet(() -> {
                    Location loc = new Location();
                    loc.setCity(request.getCity());
                    loc.setArea(request.getArea());
                    loc.setState(request.getState());
                    loc.setPincode(request.getPincode());
                    return locationRepository.save(loc);
                });

        PropertyType propertyType = propertyTypeRepository.findById(request.getPropertyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found with ID: " + request.getPropertyTypeId()));

        Set<Amenity> amenities = new HashSet<>();
        if (request.getAmenityIds() != null && !request.getAmenityIds().isEmpty()) {
            amenities = amenityRepository.findByIdIn(request.getAmenityIds());
        }

        Property property = new Property();
        property.setOwner(owner);
        property.setLocation(location);
        property.setPropertyType(propertyType);
        property.setAmenities(amenities);
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setMonthlyRent(request.getMonthlyRent());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setFurnishingStatus(request.getFurnishingStatus());
        property.setFloorNumber(request.getFloorNumber());
        property.setTotalFloors(request.getTotalFloors());
        property.setParkingAvailable(request.getParkingAvailable());
        property.setPetAllowed(request.getPetAllowed());
        property.setWaterSupply(request.getWaterSupply());
        property.setElectricityAvailable(request.getElectricityAvailable());
        property.setApprovalStatus(ApprovalStatus.PENDING);
        property.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        Property saved = propertyRepository.save(property);

        notificationService.createNotification(
                owner,
                "Property Submitted for Review",
                "Property '" + saved.getTitle() + "' was submitted and is pending admin approval.",
                "PROPERTY_SUBMITTED"
        );

        return EntityDtoMapper.toPropertyResponse(saved);
    }

    @Transactional
    public PropertyResponse updateProperty(Long id, PropertyRequest request, String ownerEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only modify properties owned by you");
        }

        Location location = locationRepository.findByCityIgnoreCaseAndAreaIgnoreCase(request.getCity(), request.getArea())
                .orElseGet(() -> {
                    Location loc = new Location();
                    loc.setCity(request.getCity());
                    loc.setArea(request.getArea());
                    loc.setState(request.getState());
                    loc.setPincode(request.getPincode());
                    return locationRepository.save(loc);
                });

        PropertyType propertyType = propertyTypeRepository.findById(request.getPropertyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found with ID: " + request.getPropertyTypeId()));

        Set<Amenity> amenities = new HashSet<>();
        if (request.getAmenityIds() != null && !request.getAmenityIds().isEmpty()) {
            amenities = amenityRepository.findByIdIn(request.getAmenityIds());
        }

        property.setLocation(location);
        property.setPropertyType(propertyType);
        property.setAmenities(amenities);
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setMonthlyRent(request.getMonthlyRent());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setFurnishingStatus(request.getFurnishingStatus());
        property.setFloorNumber(request.getFloorNumber());
        property.setTotalFloors(request.getTotalFloors());
        property.setParkingAvailable(request.getParkingAvailable());
        property.setPetAllowed(request.getPetAllowed());
        property.setWaterSupply(request.getWaterSupply());
        property.setElectricityAvailable(request.getElectricityAvailable());

        // Resubmit for review if it was previously rejected
        if (property.getApprovalStatus() == ApprovalStatus.REJECTED) {
            property.setApprovalStatus(ApprovalStatus.PENDING);
            property.setRejectionReason(null);
        }

        return EntityDtoMapper.toPropertyResponse(propertyRepository.save(property));
    }

    public List<PropertyResponse> getOwnerProperties(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return propertyRepository.findByOwnerId(owner.getId()).stream()
                .map(EntityDtoMapper::toPropertyResponse)
                .collect(Collectors.toList());
    }

    public List<PropertyResponse> searchAvailableProperties(
            String city,
            String area,
            Long propertyTypeId,
            BigDecimal minRent,
            BigDecimal maxRent,
            Integer bedrooms,
            Integer bathrooms,
            String furnishingStatus
    ) {
        List<Property> properties = propertyRepository.searchProperties(
                ApprovalStatus.APPROVED,
                AvailabilityStatus.AVAILABLE,
                city,
                area,
                propertyTypeId,
                minRent,
                maxRent,
                bedrooms,
                bathrooms,
                furnishingStatus
        );

        return properties.stream()
                .map(EntityDtoMapper::toPropertyResponse)
                .collect(Collectors.toList());
    }

    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));
        return EntityDtoMapper.toPropertyResponse(property);
    }

    @Transactional
    public void deactivateProperty(Long id, String ownerEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You can only deactivate your own properties");
        }

        property.setAvailabilityStatus(AvailabilityStatus.INACTIVE);
        propertyRepository.save(property);
    }
}
