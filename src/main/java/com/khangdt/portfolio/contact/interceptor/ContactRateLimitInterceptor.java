package com.khangdt.portfolio.contact.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContactRateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final long ONE_MINUTE_MS = 60_000L;

    private final Map<String, RequestTracker> trackerMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String clientIp = getClientIp(request);
        long now = System.currentTimeMillis();

        RequestTracker tracker = trackerMap.compute(clientIp, (ip, existingTracker) -> {
            if (existingTracker == null || (now - existingTracker.firstRequestTime) > ONE_MINUTE_MS) {
                return new RequestTracker(now, 1);
            }
            existingTracker.count++;
            return existingTracker;
        });

        if (tracker.count > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"message\":\"Too many contact submissions. Please wait a minute before trying again.\"}");
            return false;
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RequestTracker {
        final long firstRequestTime;
        int count;

        RequestTracker(long firstRequestTime, int count) {
            this.firstRequestTime = firstRequestTime;
            this.count = count;
        }
    }
}
