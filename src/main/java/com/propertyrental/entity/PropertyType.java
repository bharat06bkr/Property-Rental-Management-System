package com.propertyrental.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "property_types")
public class PropertyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g. APARTMENT, HOUSE, VILLA, PG, HOSTEL, ROOM, STUDIO, OFFICE, SHOP

    private String description;

    public PropertyType() {}

    public PropertyType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
