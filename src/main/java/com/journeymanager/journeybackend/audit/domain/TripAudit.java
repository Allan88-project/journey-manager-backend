package com.journeymanager.journeybackend.audit.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TripAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tripId;

    private String action;

    private String performedBy;

    private String role;

    private LocalDateTime timestamp;

    public TripAudit() {}

    public TripAudit(Long tripId, String action, String performedBy, String role) {
        this.tripId = tripId;
        this.action = action;
        this.performedBy = performedBy;
        this.role = role;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Long getTripId() { return tripId; }

    public String getAction() { return action; }

    public String getPerformedBy() { return performedBy; }

    public String getRole() { return role; }

    public LocalDateTime getTimestamp() { return timestamp; }
}
