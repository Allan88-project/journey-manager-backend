package com.journeymanager.journeybackend.service;

import com.journeymanager.journeybackend.model.user.User;
import com.journeymanager.journeybackend.entity.Tenant;
import com.journeymanager.journeybackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUser(String email, Tenant tenant) {
        return userRepository.findByEmailAndTenant(email, tenant)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}