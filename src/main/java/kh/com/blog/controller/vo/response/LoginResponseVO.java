package kh.com.blog.controller.vo.response;

import lombok.Data;

@Data
public class LoginResponseVO {
	private Long id;
	private String username;
	private String email;
	private String bio;
	private String profilePicture;
	private String coverImage;
	private String refreshToken;
	private String token;
}
