package com.lumierebeauty.api.common;

import java.time.Instant;
import java.util.Map;

/**
 * The standard JSON error body returned by the API.
 *
 * Example:
 * <pre>
 * {
 *   "timestamp": "2025-01-01T12:00:00Z",
 *   "status": 400,
 *   "message": "Validation failed",
 *   "fieldErrors": { "email": "must be a valid email address" }
 * }
 * </pre>
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String message,
        Map<String, String> fieldErrors
) {
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(Instant.now(), status, message, null);
    }
}
