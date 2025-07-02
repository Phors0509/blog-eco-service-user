package kh.com.blog.service;

import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RefreshTokenRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.LoginResponseDTO;
import kh.com.blog.dto.response.RefreshTokenResponseDTO;
import kh.com.blog.dto.response.UserDetailResponseDTO;

public interface UserService {
	void registerUser(RegisterRequestDTO registerRequestDTO);
	LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
	RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshToken);
	UserDetailResponseDTO getCurrentUserDetail();
}
