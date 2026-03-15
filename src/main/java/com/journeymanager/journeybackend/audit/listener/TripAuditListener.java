package com.journeymanager.journeybackend.audit.listener;

import com.journeymanager.journeybackend.audit.service.TripAuditService;
import com.journeymanager.journeybackend.trip.domain.event.TripApprovedEvent;
import com.journeymanager.journeybackend.trip.domain.event.TripCompletedEvent;
import com.journeymanager.journeybackend.trip.domain.event.TripCreatedEvent;
import com.journeymanager.journeybackend.trip.domain.event.TripStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TripAuditListener {

    private final TripAuditService auditService;

    public TripAuditListener(TripAuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void handleTripCreated(TripCreatedEvent event) {

        auditService.log(
                event.getTripId(),
                "TRIP_CREATED",
                event.getCreatedBy(),
                "USER"
        );
    }

    @EventListener
    public void handleTripApproved(TripApprovedEvent event) {

        auditService.log(
                event.getTripId(),
                "TRIP_APPROVED",
                "ADMIN",
                "ADMIN"
        );
    }

    @EventListener
    public void handleTripStarted(TripStartedEvent event) {

        auditService.log(
                event.getTripId(),
                "TRIP_STARTED",
                event.getStartedBy(),
                "USER"
        );
    }

    @EventListener
    public void handleTripCompleted(TripCompletedEvent event) {

        auditService.log(
                event.getTripId(),
                "TRIP_COMPLETED",
                event.getCompletedBy(),
                "USER"
        );
    }
}