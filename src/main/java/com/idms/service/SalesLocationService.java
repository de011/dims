package com.idms.service;

import com.idms.dto.SalesLocationDTO;
import com.idms.entity.SalesLocation;

import java.util.List;

public interface SalesLocationService {
    SalesLocation addOrUpdateSalesLocation(SalesLocationDTO salesLocationDTO);
    List<SalesLocation> getAllSalesLocations();
    SalesLocation getSalesLocationById(Integer salesLocationId);
}
