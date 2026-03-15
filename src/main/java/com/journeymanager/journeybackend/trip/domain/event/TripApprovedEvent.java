package com.journeymanager.journeybackend.trip.domain.event;

import com.journeymanager.journeybackend.common.event.DomainEvent;

public class TripApprovedEvent implements DomainEvent {

    private final Long tripId;

    public TripApprovedEvent(Long tripId) {
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }
}