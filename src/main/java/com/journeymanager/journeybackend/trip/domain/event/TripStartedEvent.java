package com.journeymanager.journeybackend.trip.domain.event;

import com.journeymanager.journeybackend.common.event.DomainEvent;

public class TripStartedEvent implements DomainEvent {

    private final Long tripId;
    private final String startedBy;

    public TripStartedEvent(Long tripId, String startedBy) {
        this.tripId = tripId;
        this.startedBy = startedBy;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getStartedBy() {
        return startedBy;
    }

}

