package com.journeymanager.journeybackend.model.user;

import com.journeymanager.journeybackend.entity.Tenant;
import com.journeymanager.journeybackend.security.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public User() {}

    public User(String email, UserRole role, Tenant tenant) {
        this.email = email;
        this.role = role;
        this.tenant = tenant;
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public UserRole getRole() { return role; }

    public Tenant getTenant() { return tenant; }

    public void setEmail(String email) { this.email = email; }

    public void setRole(UserRole role) { this.role = role; }

    public void setTenant(Tenant tenant) { this.tenant = tenant; }
}