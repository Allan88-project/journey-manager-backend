package com.journeymanager.journeybackend.trip.domain.event;

import com.journeymanager.journeybackend.common.event.DomainEvent;

public class TripCreatedEvent implements DomainEvent {

    private final Long tripId;
    private final String createdBy;

    public TripCreatedEvent(Long tripId, String createdBy) {
        this.tripId = tripId;
        this.createdBy = createdBy;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

}

