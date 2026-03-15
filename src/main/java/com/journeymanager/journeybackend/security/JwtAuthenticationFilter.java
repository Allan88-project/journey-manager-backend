package com.journeymanager.journeybackend.security;

import com.journeymanager.journeybackend.tenant.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER RECEIVED: " + authHeader);

        String token = null;
        String email = null;

        try {

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                token = authHeader.substring(7);
                System.out.println("JWT TOKEN EXTRACTED: " + token);

                email = jwtUtil.extractUsername(token);
                System.out.println("JWT USERNAME: " + email);
            }

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                CustomUserDetails userDetails =
                        (CustomUserDetails) userDetailsService.loadUserByUsername(email);

                boolean valid = jwtUtil.isTokenValid(token);
                System.out.println("JWT VALID: " + valid);

                if (valid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    System.out.println("AUTHORITIES: " + userDetails.getAuthorities());

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println("SECURITY CONTEXT AUTHENTICATED");

                    // 🔹 CRITICAL FIX — Set tenant for this request
                    TenantContext.setTenantId(userDetails.getTenantId());

                    System.out.println("TENANT CONTEXT SET: " + userDetails.getTenantId());
                }
            }

        } catch (Exception e) {

            System.out.println("JWT FILTER ERROR: " + e.getMessage());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 🔹 Always clear tenant after request
            TenantContext.clear();
        }
    }
}