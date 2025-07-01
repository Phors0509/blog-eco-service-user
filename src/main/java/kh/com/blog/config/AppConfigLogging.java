package kh.com.blog.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.com.blog.common.utils.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AppConfigLogging extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AppConfigLogging.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Generate and set trace ID for this request
		String traceId = TraceIdUtil.generateTraceId();

		// Add trace ID to response headers
		response.setHeader("X-Trace-Id", traceId);

		// Wrap request and response to cache their content
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		// Log request details with trace ID
		logger.info("REQUEST: {} {} [TraceID: {}]", request.getMethod(), request.getRequestURI(), traceId);
		logger.info("REQUEST HEADERS: {} [TraceID: {}]", getHeaders(request), traceId);

		// Continue with the filter chain
		filterChain.doFilter(requestWrapper, responseWrapper);

		// Log request body
		String requestBody = getRequestBody(requestWrapper);
		if (!requestBody.isEmpty()) {
			logger.info("REQUEST BODY: {} [TraceID: {}]", requestBody, traceId);
		}

		// Log response details
		logger.info("RESPONSE STATUS: {} [TraceID: {}]", responseWrapper.getStatus(), traceId);

		// Log response body
		String responseBody = getResponseBody(responseWrapper);
		if (!responseBody.isEmpty()) {
			logger.info("RESPONSE BODY: {} [TraceID: {}]", responseBody, traceId);
		}

		// Copy content back to the original response
		responseWrapper.copyBodyToResponse();
	}

	private String getHeaders(HttpServletRequest request) {
		StringBuilder headers = new StringBuilder();
		request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
			headers.append(headerName).append("=").append(request.getHeader(headerName)).append(", ");
		});
		return headers.toString();
	}

	private String getRequestBody(ContentCachingRequestWrapper request) {
		byte[] content = request.getContentAsByteArray();
		if (content.length > 0) {
			return new String(content, StandardCharsets.UTF_8);
		}
		return "";
	}

	private String getResponseBody(ContentCachingResponseWrapper response) {
		byte[] content = response.getContentAsByteArray();
		if (content.length > 0) {
			return new String(content, StandardCharsets.UTF_8);
		}
		return "";
	}
}
