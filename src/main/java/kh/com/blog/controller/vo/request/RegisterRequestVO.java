package kh.com.blog.controller.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.blog.common.enumeration.Gender;
import lombok.Data;

@Data
public class RegisterRequestVO {

	@NotBlank(message = "Username is required")
	private String username;
	@Email(message = "Email is not valid")
	private String email;
	@NotBlank(message = "Password is required")
	private String password;
	@NotNull(message = "Gender is required")
	private Gender gender;
}
