package org.transferservice.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserActivityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityFilter.class);

    private final UserActivityService userActivityService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true); // Always create a new session
        String sessionId = session != null ? session.getId() : "NoSession";
        if (session != null) {
            // Use the existing session for all requests
            logger.info("Using existing session with ID: {}", session.getId());
        } else {
            // Log if no session exists
            logger.warn("No session exists for request URI: {}", request.getRequestURI());
        }

//        String sessionId = session != null ? session.getId() : "NoSession";
        String email = null;
        String jwt = parseJwt(request);

        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            email = jwtUtils.getEmailFromJwtToken(jwt);
        }

        String requestURI = request.getRequestURI();
        String activityType = determineActivityType(requestURI);
        String httpMethod = request.getMethod();
        String parameters = request.getQueryString();

        try {
            // Record user activity with session information
            userActivityService.recordActivity(sessionId, email != null ? email : "register still no session", activityType, httpMethod, parameters);

            // Check for session expiration based on inactivity
            if (session != null) {
                userActivityService.checkSessionExpiration(sessionId);
            }
        } catch (Exception e) {
            logger.error("Error recording user activity", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private String determineActivityType(String requestURI) {
        if (requestURI.contains("/login")) {
            return "LOGIN";
        } else if (requestURI.contains("/logout")) {
            return "LOGOUT";
        } else if (requestURI.contains("/transfer")) {
            return "TRANSFER";
        } else if (requestURI.contains("/balance")) {
            return "CHECK_BALANCE";
        } else if (requestURI.contains("/register")) {
            return "REGISTER";
        } else {
            return "VIEW_PAGE";
        }
    }
}
