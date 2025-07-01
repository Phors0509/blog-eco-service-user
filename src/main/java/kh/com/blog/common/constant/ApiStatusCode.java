package kh.com.blog.common.constant;

import org.springframework.http.HttpStatus;

public final class ApiStatusCode {

    // HTTP Status Codes
    public static final String OK = String.valueOf(HttpStatus.OK.value());
    public static final String CREATED = String.valueOf(HttpStatus.CREATED.value());
    public static final String ACCEPTED = String.valueOf(HttpStatus.ACCEPTED.value());
    public static final String NO_CONTENT = String.valueOf(HttpStatus.NO_CONTENT.value());
    public static final String BAD_REQUEST = String.valueOf(HttpStatus.BAD_REQUEST.value());
    public static final String UNAUTHORIZED = String.valueOf(HttpStatus.UNAUTHORIZED.value());
    public static final String FORBIDDEN = String.valueOf(HttpStatus.FORBIDDEN.value());
    public static final String NOT_FOUND = String.valueOf(HttpStatus.NOT_FOUND.value());
    public static final String METHOD_NOT_ALLOWED = String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value());
    public static final String CONFLICT = String.valueOf(HttpStatus.CONFLICT.value());
    public static final String INTERNAL_SERVER_ERROR = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    public static final String SERVICE_UNAVAILABLE = String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value());

    // Custom Application Status Codes
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String BUSINESS_ERROR = "BUSINESS_ERROR";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    
    // Custom Application Status Messages
    public static final String SUCCESS_MESSAGE = "successfully";
    public static final String ERROR_MESSAGE = "An error occurred";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation error";
    public static final String BUSINESS_ERROR_MESSAGE = "Business rule violation";
    public static final String SYSTEM_ERROR_MESSAGE = "System error";
    public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found";

    // Private constructor to prevent instantiation
    private ApiStatusCode() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}