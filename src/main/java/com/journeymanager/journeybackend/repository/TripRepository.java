package com.journeymanager.journeybackend.repository;

import com.journeymanager.journeybackend.model.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    long countByTenantId(Long tenantId);

    long countByTenantIdAndStatus(Long tenantId, TripStatus status);
    List<Trip> findByTenantId(Long tenantId);

    Optional<Trip> findByIdAndTenantId(Long id, Long tenantId);
}