package kh.com.blog.controller.vo.response;

import lombok.Data;

@Data
public class RefreshTokenResponseVO {
	private String accessToken;
	private String refreshToken;
}
