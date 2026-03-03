package com.journeymanager.journeybackend.tenant;

import com.journeymanager.journeybackend.entity.Tenant;
import com.journeymanager.journeybackend.service.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.journeymanager.journeybackend.security.RoleContext;
import com.journeymanager.journeybackend.security.UserRole;
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private final TenantService tenantService;

    public TenantInterceptor(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String roleHeader = request.getHeader("X-User-Role");

        UserRole role = UserRole.USER; // default

        if (roleHeader != null) {
            try {
                role = UserRole.valueOf(roleHeader.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                role = UserRole.USER;
            }
        }

        RoleContext.setRole(role);
        String subdomain = request.getHeader("X-Tenant-Id");

        System.out.println("Header received: '" + subdomain + "'");

        if (subdomain == null || subdomain.trim().isEmpty()) {
            throw new RuntimeException("Tenant header missing");
        }

        subdomain = subdomain.trim();

        Tenant tenant = tenantService.getTenantBySubdomain(subdomain);

        // If not found, TenantService will throw RuntimeException("Tenant not found")

        TenantContext.setTenantId(tenant.getId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        TenantContext.clear();
        RoleContext.clear();
    }
}