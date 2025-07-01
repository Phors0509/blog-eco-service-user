package kh.com.blog.controller.vo;

import kh.com.blog.common.enumeration.Gender;
import lombok.Data;

@Data
public class RegisterRequestVO {
	private String username;
	private String email;
	private String password;
	private Gender gender;
}
