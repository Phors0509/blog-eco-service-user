package kh.com.blog.service;

import kh.com.blog.controller.vo.response.LoginResponseVO;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.LoginResponseDTO;

public interface UserService {
	void registerUser(RegisterRequestDTO registerRequestDTO);
	LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
}
