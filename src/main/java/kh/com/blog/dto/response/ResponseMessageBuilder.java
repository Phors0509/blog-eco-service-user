package kh.com.blog.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kh.com.blog.common.constant.ApiStatusCode;
import kh.com.blog.common.utils.TraceIdUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

public class ResponseMessageBuilder<T> {

	private final ResponseMessage<T> message = new ResponseMessage<>();

	public ResponseMessageBuilder<T> success(Runnable runnable) {
		Assert.notNull(runnable, "Runnable function cannot be null");
		runnable.run();
		return success();
	}

	public ResponseMessageBuilder<T> success() {
		this.message.result = Boolean.TRUE;
		this.message.resultCode = ApiStatusCode.OK;
		this.message.resultMessage = ApiStatusCode.SUCCESS_MESSAGE;
		this.message.status = HttpStatus.OK;
		return this;
	}

	public ResponseMessageBuilder<T> fail(final String code, final String message) {
		this.message.result = Boolean.FALSE;
		this.message.resultCode = code;
		this.message.resultMessage = message;
		this.message.status = HttpStatus.BAD_REQUEST;
		return this;
	}

	/**
	 * Add Error Code
	 *
	 * @param code
	 * @param message
	 * @return
	 */
	public ResponseMessageBuilder<T> addCode(final String code, final String message) {
		this.message.setResultCode(code);
		this.message.setResultMessage(message);
		return this;
	}

	/**
	 * Add Response Data
	 *
	 * @param data
	 * @return
	 */
	public ResponseMessageBuilder<T> addData(final T data) {
		this.message.setBody(data);
		return this;
	}

	/**
	 * Finally call to construct ResponseMessage
	 *
	 * @return
	 */
	public final ResponseMessage<T> build() {
		return message;
	}

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Schema(description = "Standard response wrapper for all API responses")
	public static class ResponseMessage<T> {

		private static final long serialVersionUID = 127922628199902941L;

		@Schema(description = "Indicates whether the operation was successful", example = "true")
		private Boolean result = false;

		@Schema(description = "Status code of the response", example = "200")
		private String resultCode;

		@Schema(description = "Human-readable message describing the result", example = "Operation completed successfully")
		private String resultMessage;

		@Schema(description = "Additional error details in case of failure", nullable = true)
		private Object resultError;

		@Schema(description = "Unique trace ID for request tracking", example = "a1b2c3d4e5f6")
		private String traceId;

		@Schema(description = "Response payload data")
		private transient T body;

		@JsonIgnore
		private String error;

		@JsonIgnore
		private HttpStatus status;

		public ResponseMessage() {
		}

		public String getTraceId() {
			if (StringUtils.isEmpty(traceId))
				traceId = TraceIdUtil.getTraceId();
			return traceId;
		}

		public ResponseMessage<T> body(T body) {
			this.body = body;
			return this;
		}

		public ResponseMessage<T> status(HttpStatus status) {
			this.status = status;
			return this;
		}

		public ResponseMessage<T> error(String error) {
			this.error = error;
			return this;
		}

		public ResponseMessage<T> traceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		public static <T> ResponseMessage<T> success() {
			return success(ApiStatusCode.OK, ApiStatusCode.SUCCESS_MESSAGE);
		}

		public static <T> ResponseMessage<T> success(T body) {
			return success(ApiStatusCode.SUCCESS_MESSAGE, body);
		}

		@SuppressWarnings("unchecked")
		public static <T> ResponseMessage<T> success(String message, T body) {
			return (ResponseMessage<T>) success(ApiStatusCode.OK, message).body(body);
		}

		public static <T> ResponseMessage<T> success(String code, String message) {
			return build(code, message, true, HttpStatus.OK);
		}

		public static <T> ResponseMessage<T> success(String code, String message, T body) {
			ResponseMessage<T> obj = build(code, message, true, HttpStatus.OK);
			obj.body = body;
			return obj;
		}

		public static <T> ResponseMessage<T> fail(String code, String message) {
			return build(code, message, false, HttpStatus.BAD_REQUEST);
		}

		public static <T> ResponseMessage<T> fail(String code, String message, Object resultError) {
			ResponseMessage<T> obj = new ResponseMessage<>();
			obj.resultCode = code;
			obj.status = HttpStatus.BAD_REQUEST;
			obj.resultMessage = message;
			obj.resultError = resultError;
			return obj;
		}

		public static <T> ResponseMessage<T> build(String code, String message, boolean result, HttpStatus status) {
			return build(code, message, result, status, null);
		}

		public static <T> ResponseMessage<T> build(String code, String message, boolean result, HttpStatus status, T body) {
			ResponseMessage<T> obj = new ResponseMessage<>();
			obj.result = result;
			obj.resultCode = code;
			obj.resultMessage = message;
			obj.status = status;
			obj.body = body;
			return obj;
		}
	}
}
