package com.journeymanager.journeybackend.service;

import com.journeymanager.journeybackend.model.trip.Trip;
import com.journeymanager.journeybackend.model.trip.TripStatus;
import com.journeymanager.journeybackend.repository.TripRepository;
import com.journeymanager.journeybackend.security.RoleContext;
import com.journeymanager.journeybackend.security.UserRole;
import com.journeymanager.journeybackend.exception.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip create(Trip trip) {
        trip.setStatus(TripStatus.PENDING);
        return tripRepository.save(trip);
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip updateStatus(Long tripId, TripStatus newStatus) {

        // ✅ RBAC Enforcement
        if (RoleContext.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can approve or reject trips");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // ✅ Lifecycle Enforcement (unchanged)
        if (trip.getStatus() != TripStatus.PENDING) {
            throw new IllegalStateException("Trip already finalized");
        }

        trip.setStatus(newStatus);

        return tripRepository.save(trip);
    }
}