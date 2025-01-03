package com.idms.service.impl;

import com.idms.exception.DuplicateSalesLocationException;
import com.idms.exception.SalesLocationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.idms.dto.SalesLocationDTO;
import com.idms.entity.SalesLocation;
import com.idms.repo.SalesLocationRepository;
import com.idms.service.SalesLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesLocationServiceImpl implements SalesLocationService {
    private static final Logger logger = LoggerFactory.getLogger(SalesLocationServiceImpl.class);

    @Autowired
    private SalesLocationRepository salesLocationRepository;

    @Override
    public SalesLocation addOrUpdateSalesLocation(SalesLocationDTO salesLocationDTO) {
        if (salesLocationDTO == null) {
            throw new IllegalArgumentException("SalesLocationDTO cannot be null");
        }
        Optional<SalesLocation> existingSalesLocation = salesLocationRepository
                .findByNameAndCity(salesLocationDTO.getName(), salesLocationDTO.getCity());

        if (existingSalesLocation.isPresent()) {
            throw new DuplicateSalesLocationException(
                    "Duplicate SalesLocation found with name: " + salesLocationDTO.getName() + " and city: " + salesLocationDTO.getCity());
        }
        SalesLocation salesLocation;
        if (salesLocationDTO.getSalesLocationId() != null) {
            salesLocation = salesLocationRepository.findById(salesLocationDTO.getSalesLocationId())
                    .orElse(new SalesLocation());
        } else {
            salesLocation = new SalesLocation();
        }

        salesLocation.setName(salesLocationDTO.getName());
        salesLocation.setCity(salesLocationDTO.getCity());

        try {
            SalesLocation savedSalesLocation = salesLocationRepository.save(salesLocation);
            logger.info("SalesLocation added/updated: {}", savedSalesLocation);
            return savedSalesLocation;
        } catch (Exception ex) {
            logger.error("Error saving SalesLocation: {}", ex.getMessage());
            throw new RuntimeException("Error saving SalesLocation", ex);
        }
    }



    @Override
    public List<SalesLocation> getAllSalesLocations() {
        try {
            return salesLocationRepository.findAll();
        } catch (Exception ex) {
            logger.error("Error retrieving all SalesLocations: {}", ex.getMessage());
            throw new RuntimeException("Error retrieving all SalesLocations", ex);
        }
    }

    @Override
    public SalesLocation getSalesLocationById(Integer salesLocationId) {
        try {
            return salesLocationRepository.findById(salesLocationId).orElseThrow(() -> new SalesLocationNotFoundException("SalesLocation not found for ID: " + salesLocationId));
        } catch (SalesLocationNotFoundException ex) {
            logger.error("SalesLocation not found for ID: {}", salesLocationId);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error retrieving SalesLocation by ID: {}", ex.getMessage());
            throw new RuntimeException("Error retrieving SalesLocation by ID", ex);
        }
    }
}
