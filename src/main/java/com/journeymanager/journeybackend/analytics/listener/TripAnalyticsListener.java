package com.journeymanager.journeybackend.analytics.listener;

import com.journeymanager.journeybackend.analytics.service.AdminAnalyticsService;
import com.journeymanager.journeybackend.trip.domain.event.TripCompletedEvent;
import com.journeymanager.journeybackend.trip.domain.event.TripCreatedEvent;
import com.journeymanager.journeybackend.trip.domain.event.TripStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TripAnalyticsListener {

    private final AdminAnalyticsService analyticsService;

    public TripAnalyticsListener(AdminAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @EventListener
    public void handleTripCreated(TripCreatedEvent event) {
        analyticsService.refreshCache();
    }

    @EventListener
    public void handleTripStarted(TripStartedEvent event) {
        analyticsService.refreshCache();
    }

    @EventListener
    public void handleTripCompleted(TripCompletedEvent event) {
        analyticsService.refreshCache();
    }

}

