package kh.com.blog.dto.response;

import lombok.Data;

@Data
public class RefreshTokenResponseDTO {
	private String accessToken;
	private String refreshToken;
}
