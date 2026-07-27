package com.spendwiseai.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory sliding-window rate limiter for AI endpoints.
 * Allows configurable requests per minute per user (identified by Authorization header).
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long WINDOW_MS = 60_000L;

    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Only rate-limit AI endpoints
        return !request.getRequestURI().startsWith("/api/ai/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userKey = extractUserKey(request);
        WindowCounter counter = counters.computeIfAbsent(userKey, k -> new WindowCounter());

        if (!counter.tryAcquire()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"message\":\"Too many AI requests. Please wait a moment and try again.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractUserKey(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            // Use last 16 chars of token as key to avoid storing full tokens
            String token = auth.substring(7);
            return token.length() > 16 ? token.substring(token.length() - 16) : token;
        }
        return request.getRemoteAddr();
    }

    private static class WindowCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_MS) {
                synchronized (this) {
                    if (now - windowStart > WINDOW_MS) {
                        windowStart = now;
                        count.set(0);
                    }
                }
            }
            return count.incrementAndGet() <= MAX_REQUESTS_PER_MINUTE;
        }
    }
}
