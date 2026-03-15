package com.journeymanager.journeybackend.trip.location.dto;

public class LocationUpdateRequest {

    private Double latitude;
    private Double longitude;
    private Double speed;
    private Double heading;

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
