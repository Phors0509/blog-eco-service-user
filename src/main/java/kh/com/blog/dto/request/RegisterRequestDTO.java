package kh.com.blog.dto.request;

import kh.com.blog.common.enumeration.Gender;
import lombok.Data;

@Data
public class RegisterRequestDTO {
	private String username;
	private String email;
	private String password;
	private Gender gender;
}
