package com.journeymanager.journeybackend.trip.location.controller;

import com.journeymanager.journeybackend.dto.ApiResponse;
import com.journeymanager.journeybackend.trip.location.dto.LocationUpdateRequest;
import com.journeymanager.journeybackend.trip.location.service.LocationTrackingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripLocationController {

    private static final Logger log =
            LoggerFactory.getLogger(TripLocationController.class);

    private final LocationTrackingService locationTrackingService;

    public TripLocationController(LocationTrackingService locationTrackingService) {
        this.locationTrackingService = locationTrackingService;
    }

    /*
     * USER SEND GPS LOCATION
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{tripId}/location")
    public ResponseEntity<ApiResponse<String>> updateLocation(
            @PathVariable Long tripId,
            @RequestBody LocationUpdateRequest request
    ) {

        log.info(
                "GPS update received -> tripId={}, lat={}, lon={}, speed={}, heading={}",
                tripId,
                request.getLatitude(),
                request.getLongitude(),
                request.getSpeed(),
                request.getHeading()
        );

        locationTrackingService.recordLocation(
                tripId,
                request.getLatitude(),
                request.getLongitude(),
                request.getSpeed(),
                request.getHeading()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Location recorded")
        );
    }
}