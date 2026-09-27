package com.propertyrental.util;

import com.propertyrental.entity.*;
import com.propertyrental.entity.Enums.ApprovalStatus;
import com.propertyrental.entity.Enums.AvailabilityStatus;
import com.propertyrental.entity.Enums.OwnerStatus;
import com.propertyrental.entity.Enums.Role;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedPropertyTypes();
        seedAmenities();
        seedLocations();
        seedSampleProperty();
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@propertyrental.com")) {
            User admin = new User();
            admin.setFullName("Platform System Admin");
            admin.setEmail("admin@propertyrental.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("9998887770");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("owner1@gmail.com")) {
            User owner1 = new User();
            owner1.setFullName("Rajesh Verma (Approved Owner)");
            owner1.setEmail("owner1@gmail.com");
            owner1.setPassword(passwordEncoder.encode("owner123"));
            owner1.setPhone("9876543210");
            owner1.setRole(Role.OWNER);
            owner1.setOwnerStatus(OwnerStatus.APPROVED);
            userRepository.save(owner1);
        }

        if (!userRepository.existsByEmail("owner2@gmail.com")) {
            User owner2 = new User();
            owner2.setFullName("Suresh Kumar (Pending Owner)");
            owner2.setEmail("owner2@gmail.com");
            owner2.setPassword(passwordEncoder.encode("owner123"));
            owner2.setPhone("9876543211");
            owner2.setRole(Role.OWNER);
            owner2.setOwnerStatus(OwnerStatus.PENDING);
            userRepository.save(owner2);
        }

        if (!userRepository.existsByEmail("tenant1@gmail.com")) {
            User tenant1 = new User();
            tenant1.setFullName("Anita Sharma (Tenant)");
            tenant1.setEmail("tenant1@gmail.com");
            tenant1.setPassword(passwordEncoder.encode("tenant123"));
            tenant1.setPhone("9123456789");
            tenant1.setRole(Role.TENANT);
            userRepository.save(tenant1);
        }
    }

    private void seedPropertyTypes() {
        if (propertyTypeRepository.count() == 0) {
            List<String> types = Arrays.asList(
                    "APARTMENT", "HOUSE", "VILLA", "PG", "HOSTEL", "ROOM", "STUDIO", "OFFICE", "SHOP"
            );
            for (String typeName : types) {
                PropertyType pt = new PropertyType();
                pt.setName(typeName);
                pt.setDescription("Property categorization for " + typeName);
                propertyTypeRepository.save(pt);
            }
        }
    }

    private void seedAmenities() {
        if (amenityRepository.count() == 0) {
            List<String> amenities = Arrays.asList(
                    "PARKING", "WIFI", "LIFT", "GYM", "SWIMMING_POOL",
                    "POWER_BACKUP", "SECURITY", "CCTV", "FURNISHED", "AIR_CONDITIONING"
            );
            for (String amenityName : amenities) {
                Amenity a = new Amenity();
                a.setName(amenityName);
                a.setDescription("Facility: " + amenityName);
                amenityRepository.save(a);
            }
        }
    }

    private void seedLocations() {
        if (locationRepository.count() == 0) {
            locationRepository.save(new Location(null, "Hyderabad", "Banjara Hills", "Telangana", "500034"));
            locationRepository.save(new Location(null, "Hyderabad", "Gachibowli", "Telangana", "500032"));
            locationRepository.save(new Location(null, "Bangalore", "Indiranagar", "Karnataka", "560038"));
            locationRepository.save(new Location(null, "Chennai", "Velachery", "Tamil Nadu", "600042"));
            locationRepository.save(new Location(null, "Tirupati", "RMR Park", "Andhra Pradesh", "517501"));
            locationRepository.save(new Location(null, "Vijayawada", "Benz Circle", "Andhra Pradesh", "520010"));
        }
    }

    private void seedSampleProperty() {
        if (propertyRepository.count() == 0) {
            User owner = userRepository.findByEmail("owner1@gmail.com").orElse(null);
            Location loc = locationRepository.findByCityIgnoreCaseAndAreaIgnoreCase("Hyderabad", "Banjara Hills").orElse(null);
            PropertyType pt = propertyTypeRepository.findByNameIgnoreCase("APARTMENT").orElse(null);
            List<Amenity> allAmenities = amenityRepository.findAll();

            if (owner != null && loc != null && pt != null) {
                Property prop = new Property();
                prop.setOwner(owner);
                prop.setLocation(loc);
                prop.setPropertyType(pt);
                prop.setAmenities(new HashSet<>(allAmenities));
                prop.setTitle("Luxury 2BHK Apartment in Banjara Hills");
                prop.setDescription("Spacious 2BHK apartment with modern interiors, 24/7 security, power backup, modular kitchen, and balcony view.");
                prop.setMonthlyRent(new BigDecimal("22000.00"));
                prop.setSecurityDeposit(new BigDecimal("44000.00"));
                prop.setBedrooms(2);
                prop.setBathrooms(2);
                prop.setAreaSqFt(1250.0);
                prop.setFurnishingStatus("FULLY_FURNISHED");
                prop.setFloorNumber(3);
                prop.setTotalFloors(5);
                prop.setParkingAvailable(true);
                prop.setPetAllowed(true);
                prop.setWaterSupply("24 Hours Municipal & Borewell");
                prop.setElectricityAvailable("24 Hours with 100% DG Backup");
                prop.setApprovalStatus(ApprovalStatus.APPROVED);
                prop.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

                propertyRepository.save(prop);
            }
        }
    }
}
