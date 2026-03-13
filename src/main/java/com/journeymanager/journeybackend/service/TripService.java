package com.journeymanager.journeybackend.service;

import com.journeymanager.journeybackend.audit.service.TripAuditService;
import com.journeymanager.journeybackend.trip.domain.Trip;
import com.journeymanager.journeybackend.trip.domain.TripStatus;
import com.journeymanager.journeybackend.trip.domain.TripStateMachine;
import com.journeymanager.journeybackend.repository.TripRepository;
import com.journeymanager.journeybackend.security.CustomUserDetails;
import com.journeymanager.journeybackend.trip.domain.event.TripCreatedEvent;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripAuditService auditService;

    public TripService(
            TripRepository tripRepository,
            TripAuditService auditService
    ) {
        this.tripRepository = tripRepository;
        this.auditService = auditService;
    }

    /*
     * TENANT CONTEXT
     */

    private CustomUserDetails getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (CustomUserDetails) authentication.getPrincipal();
    }

    private Long getCurrentTenantId() {
        return getCurrentUser().getTenantId();
    }

    private String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /*
     * BASIC OPERATIONS
     */

    public List<Trip> findAll() {

        Long tenantId = getCurrentTenantId();

        return tripRepository.findByTenantId(tenantId);
    }

    public Trip create(Trip trip) {

        eventPublisher.publish(
                new TripCreatedEvent(saved.getId(), getCurrentUsername())
        );
        Trip saved = tripRepository.save(trip);

        auditService.log(
                saved.getId(),
                "TRIP_CREATED",
                getCurrentUsername(),
                "USER"
        );

        return saved;
    }

    /*
     * TENANT SAFE TRIP FETCH
     */

    private Trip getTripOrThrow(Long id) {

        Long tenantId = getCurrentTenantId();

        return tripRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
    }

    /*
     * ADMIN ACTIONS
     */

    public Trip approveTrip(Long id) {

        Trip trip = getTripOrThrow(id);

        TripStateMachine.validateApprove(trip);

        trip.setStatus(TripStatus.APPROVED);

        Trip saved = tripRepository.save(trip);

        auditService.log(
                saved.getId(),
                "TRIP_APPROVED",
                getCurrentUsername(),
                "ADMIN"
        );

        return saved;
    }

    public Trip rejectTrip(Long id) {

        Trip trip = getTripOrThrow(id);

        TripStateMachine.validateReject(trip);

        trip.setStatus(TripStatus.REJECTED);

        return tripRepository.save(trip);
    }

    /*
     * USER ACTIONS
     */

    public Trip startTrip(Long tripId) {

        Trip trip = getTripOrThrow(tripId);

        TripStateMachine.validateStart(trip);

        trip.setStatus(TripStatus.IN_PROGRESS);
        trip.setStartedAt(LocalDateTime.now());

        Trip saved = tripRepository.save(trip);

        auditService.log(
                saved.getId(),
                "TRIP_STARTED",
                getCurrentUsername(),
                "USER"
        );

        return saved;
    }

    public Trip completeTrip(Long tripId) {

        Trip trip = getTripOrThrow(tripId);

        TripStateMachine.validateComplete(trip);

        trip.setStatus(TripStatus.COMPLETED);
        trip.setCompletedAt(LocalDateTime.now());

        Trip saved = tripRepository.save(trip);

        auditService.log(
                saved.getId(),
                "TRIP_COMPLETED",
                getCurrentUsername(),
                "USER"
        );

        return saved;
    }

    public Trip emergencyTrip(Long tripId) {

        Trip trip = getTripOrThrow(tripId);

        TripStateMachine.validateEmergency(trip);

        trip.setStatus(TripStatus.EMERGENCY);

        return tripRepository.save(trip);
    }

}
