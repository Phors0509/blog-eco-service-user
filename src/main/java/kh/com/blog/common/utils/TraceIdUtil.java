package kh.com.blog.common.utils;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdUtil {

    private static final String TRACE_ID_KEY = "traceId";

    /**
     * Generates a new trace ID and sets it in the MDC.
     * @return the generated trace ID
     */
    public static String generateTraceId() {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put(TRACE_ID_KEY, traceId);
        return traceId;
    }

    /**
     * Sets an existing trace ID in the MDC.
     * @param traceId the trace ID to set
     */
    public static void setTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    /**
     * Gets the current trace ID from the MDC.
     * @return the current trace ID, or null if not set
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * Clears the trace ID from the MDC.
     */
    public static void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }
}