package com.journeymanager.journeybackend.service;

import com.journeymanager.journeybackend.audit.domain.TripAudit;
import com.journeymanager.journeybackend.repository.TripAuditRepository;
import org.springframework.stereotype.Service;

@Service
public class TripAuditService {

    private final TripAuditRepository repository;

    public TripAuditService(TripAuditRepository repository) {
        this.repository = repository;
    }

    public void log(Long tripId, String action, String user, String role) {

        TripAudit audit = new TripAudit(
                tripId,
                action,
                user,
                role
        );

        repository.save(audit);
    }
}
