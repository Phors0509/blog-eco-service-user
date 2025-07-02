package kh.com.blog.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailResponseDTO {
	private Long id;
	private String username;
	private String email;
	private String role;
	private Boolean isVerified;
	private String accountLevel;
	private String profilePicture;
	private String coverImage;
	private String bio;
	private String location;
	private Boolean emailNotifications;
	private String gender;
	private LocalDateTime lastLogin;
	private LocalDateTime createdAt;
}
