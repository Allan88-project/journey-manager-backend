package com.journeymanager.journeybackend.trip.domain.event;

import com.journeymanager.journeybackend.common.event.DomainEvent;

public class TripCompletedEvent implements DomainEvent {

    private final Long tripId;
    private final String completedBy;

    public TripCompletedEvent(Long tripId, String completedBy) {
        this.tripId = tripId;
        this.completedBy = completedBy;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getCompletedBy() {
        return completedBy;
    }

}

