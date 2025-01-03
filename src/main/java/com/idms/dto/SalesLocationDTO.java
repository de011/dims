package com.idms.dto;

public class SalesLocationDTO {
    private Integer salesLocationId;
    private String name;
    private String city;


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
