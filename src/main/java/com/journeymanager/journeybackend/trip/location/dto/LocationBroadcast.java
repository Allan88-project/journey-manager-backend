package com.journeymanager.journeybackend.trip.location.dto;

public class LocationBroadcast {

    private Long tripId;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Double heading;

    public LocationBroadcast(
            Long tripId,
            Double latitude,
            Double longitude,
            Double speed,
            Double heading
    ) {
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
    }

    public Long getTripId() {
        return tripId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getHeading() {
        return heading;
    }
}
