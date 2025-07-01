package kh.com.blog.service;

import kh.com.blog.controller.vo.LoginRequestVO;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;

public interface UserService {
	void registerUser(RegisterRequestDTO registerRequestDTO);
	void loginUser(LoginRequestDTO loginRequestDTO);
}
