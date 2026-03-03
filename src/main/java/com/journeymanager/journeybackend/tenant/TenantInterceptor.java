package com.journeymanager.journeybackend.tenant;

import com.journeymanager.journeybackend.entity.Tenant;
import com.journeymanager.journeybackend.model.user.User;
import com.journeymanager.journeybackend.security.RoleContext;
import com.journeymanager.journeybackend.service.TenantService;
import com.journeymanager.journeybackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.journeymanager.journeybackend.tenant.TenantContext;
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private final TenantService tenantService;
    private final UserService userService;

    public TenantInterceptor(TenantService tenantService,
                             UserService userService) {
        this.tenantService = tenantService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 1️⃣ Resolve Tenant
        String tenantHeader = request.getHeader("X-Tenant-Id");
        if (tenantHeader == null || tenantHeader.isBlank()) {
            throw new RuntimeException("Missing X-Tenant-Id header");
        }

        Tenant tenant = tenantService.getTenantBySubdomain(tenantHeader);

// VERY IMPORTANT
        TenantContext.setTenantId(tenant.getId());

        // 2️⃣ Resolve User Email
        String email = request.getHeader("X-User-Email");
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Missing X-User-Email header");
        }

        // 3️⃣ Load User from DB
        User user = userService.loadUser(email, tenant);

        // 4️⃣ Set RoleContext FROM DATABASE
        RoleContext.setRole(user.getRole());

        return true;
    }
}