package com.journeymanager.journeybackend.repository;

import com.journeymanager.journeybackend.model.user.User;
import com.journeymanager.journeybackend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndTenant(String email, Tenant tenant);
}