package com.journeymanager.journeybackend.trip.location.service;

import com.journeymanager.journeybackend.repository.TripRepository;
import com.journeymanager.journeybackend.trip.domain.Trip;
import com.journeymanager.journeybackend.trip.domain.TripStatus;
import com.journeymanager.journeybackend.trip.location.domain.TripLocation;
import com.journeymanager.journeybackend.trip.location.dto.LocationBroadcast;
import com.journeymanager.journeybackend.trip.location.repository.TripLocationRepository;
import com.journeymanager.journeybackend.tenant.TenantContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationTrackingService {

    private static final Logger log =
            LoggerFactory.getLogger(LocationTrackingService.class);

    private final TripLocationRepository tripLocationRepository;
    private final TripRepository tripRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public LocationTrackingService(
            TripLocationRepository tripLocationRepository,
            TripRepository tripRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.tripLocationRepository = tripLocationRepository;
        this.tripRepository = tripRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void recordLocation(
            Long tripId,
            Double latitude,
            Double longitude,
            Double speed,
            Double heading
    ) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            log.error("TenantContext returned NULL. Cannot record location.");
            throw new RuntimeException("Tenant not resolved from security context");
        }

        log.info("Recording GPS location -> tenantId={}, tripId={}, lat={}, lon={}",
                tenantId,
                tripId,
                latitude,
                longitude
        );

        Trip trip = tripRepository
                .findByIdAndTenantId(tripId, tenantId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Location updates allowed only when trip is IN_PROGRESS"
            );
        }

        TripLocation location = new TripLocation();
        location.setTripId(tripId);
        location.setTenantId(tenantId);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setSpeed(speed);
        location.setHeading(heading);

        tripLocationRepository.save(location);

        log.info("Location saved to database for tripId={}", tripId);

        // Broadcast real-time update
        LocationBroadcast broadcast = new LocationBroadcast(
                tripId,
                latitude,
                longitude,
                speed,
                heading
        );

        messagingTemplate.convertAndSend(
                "/topic/trips/" + tripId + "/location",
                broadcast
        );

        log.info("Location broadcast sent via WebSocket for tripId={}", tripId);
    }
}