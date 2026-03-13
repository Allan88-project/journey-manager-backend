package com.journeymanager.journeybackend.audit.listener;

import com.journeymanager.journeybackend.audit.service.TripAuditService;
import com.journeymanager.journeybackend.trip.domain.event.TripCreatedEvent;
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

}

