package com.journeymanager.journeybackend.security;

public class RoleContext {

    private static final ThreadLocal<UserRole> currentRole = new ThreadLocal<>();

    private RoleContext() {
        // Prevent instantiation
    }

    public static void setRole(UserRole role) {
        currentRole.set(role);
    }

    public static UserRole getRole() {
        UserRole role = currentRole.get();
        return (role != null) ? role : UserRole.USER;
    }

    public static void clear() {
        currentRole.remove();
    }
}