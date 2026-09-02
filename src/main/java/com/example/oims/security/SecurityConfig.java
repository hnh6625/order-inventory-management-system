package com.example.oims.security;

import com.example.oims.security.infrastructure.filter.ApiKeyAuthenticationFilter;
import com.example.oims.security.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ApiKeyAuthenticationFilter apiKeyFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          ApiKeyAuthenticationFilter apiKeyFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.apiKeyFilter = apiKeyFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Webhook endpoints — xác thực bằng API Key
                        .requestMatchers("/webhooks/**").hasRole("MARKETPLACE")
                        // Inventory — chỉ WAREHOUSE_STAFF và SYSTEM_ADMIN
                        .requestMatchers("/api/inventory/**")
                        .hasAnyRole("WAREHOUSE_STAFF", "SYSTEM_ADMIN")
                        // Catalog — chỉ SYSTEM_ADMIN
                        .requestMatchers("/api/styles/**")
                        .hasRole("SYSTEM_ADMIN")
                        // Orders — OPERATIONS_STAFF và SYSTEM_ADMIN
                        .requestMatchers("/api/orders/**")
                        .hasAnyRole("OPERATIONS_STAFF", "SYSTEM_ADMIN")
                        // Fulfillment — OPERATIONS_STAFF và SYSTEM_ADMIN
                        .requestMatchers("/api/fulfillment/**")
                        .hasAnyRole("OPERATIONS_STAFF", "SYSTEM_ADMIN")
                        .anyRequest().authenticated()
                )
                // Thêm filter theo thứ tự
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, ApiKeyAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}