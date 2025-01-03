package com.idms.repo;

import com.idms.entity.SalesLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesLocationRepository extends JpaRepository<SalesLocation, Integer> {
    Optional<SalesLocation> findByNameAndCity(String name, String city);
}
