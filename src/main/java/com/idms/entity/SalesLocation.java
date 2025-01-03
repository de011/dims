package com.idms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sales_locations")
public class SalesLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer salesLocationId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    // Getters and Setters
    public Integer getSalesLocationId() {
        return salesLocationId;
    }

    public void setSalesLocationId(Integer salesLocationId) {
        this.salesLocationId = salesLocationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}