package com.example.oims.security.infrastructure.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final Map<String, String> apiKeys;
    private final Map<String, String> endpointChannels;

    public ApiKeyAuthenticationFilter(
            @Value("${app.security.api-keys.shopee}") String shopeeApiKey,
            @Value("${app.security.api-keys.tiktok}") String tiktokApiKey) {

        this.apiKeys = Map.of(
                shopeeApiKey, "SHOPEE",
                tiktokApiKey, "TIKTOK"
        );

        this.endpointChannels = Map.of(
                "/webhooks/shopee/", "SHOPEE",
                "/webhooks/tiktokshop/", "TIKTOK"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/webhooks/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-Key");

        if (apiKey == null || !apiKeys.containsKey(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"Invalid API Key\"}");
            return;
        }

        String channel = apiKeys.get(apiKey);

        if (!isChannelAllowed(request.getRequestURI(), channel)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        channel,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_MARKETPLACE"))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isChannelAllowed(String requestUri, String channel) {
        return endpointChannels.entrySet().stream()
                .filter(entry -> requestUri.startsWith(entry.getKey()))
                .findFirst()
                .map(entry -> entry.getValue().equals(channel))
                .orElse(false);
    }
}
