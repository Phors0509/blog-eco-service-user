package kh.com.blog.exception;

import kh.com.blog.common.constant.ApiStatusCode;
import kh.com.blog.dto.response.ResponseMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Handles BusinessException.
	 * Uses the status code and error code from the exception.
	 *
	 * @param ex the BusinessException
	 * @return a ResponseMessage with the appropriate error information
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseEntity<ResponseMessageBuilder.ResponseMessage<Object>> handleBusinessException(BusinessException ex) {
		// Log the business exception
		logger.warn("BUSINESS ERROR: {}", ex.getMessage(), ex);
		// Create a standardized error response with trace ID
		ResponseMessageBuilder.ResponseMessage<Object> response = new ResponseMessageBuilder<>()
				.fail(ex.getErrorCode(), ex.getMessage())
				.build();

		// Return with the correct HTTP status code from the exception
		return new ResponseEntity<>(response, ex.getStatus());
	}

	/**
	 * Handles all other exceptions.
	 *
	 * @param ex the Exception
	 * @return a ResponseMessage with the appropriate error information
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ResponseMessageBuilder.ResponseMessage<Object>> handleAllExceptions(Exception ex) {
		// Log the exception
		logger.error("ERROR: {}", ex.getMessage(), ex);

		// Create a standardized error response with trace ID
		ResponseMessageBuilder.ResponseMessage<Object> response = new ResponseMessageBuilder<>()
				.fail(ApiStatusCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage())
				.build();

		// Return with the correct HTTP status code
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
