package com.journeymanager.journeybackend.trip.location.repository;

import com.journeymanager.journeybackend.trip.location.domain.TripLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripLocationRepository extends JpaRepository<TripLocation, Long> {

    List<TripLocation> findByTripId(Long tripId);

}
