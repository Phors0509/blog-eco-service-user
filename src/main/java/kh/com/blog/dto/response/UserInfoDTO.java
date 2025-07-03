package kh.com.blog.dto.response;

import kh.com.blog.common.enumeration.AccountLevel;
import kh.com.blog.common.enumeration.Role;
import lombok.Data;

@Data
public class UserInfoDTO {
	private Long id;
	private String username;
	private String email;
	private Role role;
	private Boolean isVerified;
	private AccountLevel accountLevel;
}
