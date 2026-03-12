package com.journeymanager.journeybackend.controller;

import com.journeymanager.journeybackend.model.audit.TripAudit;
import com.journeymanager.journeybackend.repository.TripAuditRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripAuditController {

    private final TripAuditRepository auditRepository;

    public TripAuditController(TripAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("/{tripId}/timeline")
    public List<TripAudit> getTripTimeline(@PathVariable Long tripId) {
        return auditRepository.findByTripIdOrderByTimestampAsc(tripId);
    }
}
