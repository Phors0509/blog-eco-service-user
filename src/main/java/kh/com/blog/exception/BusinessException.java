package kh.com.blog.exception;

import kh.com.blog.common.constant.ApiStatusCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception for business rule violations.
 * This exception is thrown when a business rule is violated.
 * The default HTTP status code is 400 (Bad Request).
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus status;

    /**
     * Constructs a new BusinessException with the specified error message.
     * The default error code is ApiStatusCode.BAD_REQUEST, and the default status is HttpStatus.BAD_REQUEST.
     *
     * @param message the error message
     */
    public BusinessException(String message) {
        this(ApiStatusCode.BAD_REQUEST, message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Constructs a new BusinessException with the specified error code and message.
     * The default status is HttpStatus.BAD_REQUEST.
     *
     * @param errorCode the error code
     * @param message the error message
     */
    public BusinessException(String errorCode, String message) {
        this(errorCode, message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Constructs a new BusinessException with the specified error code, message, and status.
     *
     * @param errorCode the error code
     * @param message the error message
     * @param status the HTTP status
     */
    public BusinessException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    /**
     * Constructs a new BusinessException with the specified error code, message, and cause.
     * The default status is HttpStatus.BAD_REQUEST.
     *
     * @param errorCode the error code
     * @param message the error message
     * @param cause the cause
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, HttpStatus.BAD_REQUEST, cause);
    }

    /**
     * Constructs a new BusinessException with the specified error code, message, status, and cause.
     *
     * @param errorCode the error code
     * @param message the error message
     * @param status the HTTP status
     * @param cause the cause
     */
    public BusinessException(String errorCode, String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }
}