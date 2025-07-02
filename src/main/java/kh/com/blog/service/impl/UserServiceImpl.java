package kh.com.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.blog.common.utils.JwtUtil;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RefreshTokenRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.LoginResponseDTO;
import kh.com.blog.dto.response.RefreshTokenResponseDTO;
import kh.com.blog.dto.response.UserInfoDTO;
import kh.com.blog.entity.UserEntity;
import kh.com.blog.exception.BusinessException;
import kh.com.blog.repository.UserRepository;
import kh.com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ObjectMapper objectMapper;
	private final JwtUtil jwtUtil;

	@Override
	public void registerUser(RegisterRequestDTO registerRequestDTO) {
		try {
			// find a user by username or email
			UserEntity userEntity = userRepository.findByUsernameOrEmail(registerRequestDTO.getUsername(), registerRequestDTO.getEmail());
			// if a user exists, throw BusinessException
			if (ObjectUtils.isNotEmpty(userEntity)) {
				if (registerRequestDTO.getUsername().equals(userEntity.getUsername())) {
					throw new BusinessException("Username already exists.");
				}
				if (registerRequestDTO.getEmail().equals(userEntity.getEmail())) {
					throw new BusinessException("Email already exists.");
				}
			}
			// convert RegisterRequestDTO to UserEntity
			UserEntity user = objectMapper.convertValue(registerRequestDTO, UserEntity.class);
			// set password using PasswordEncoder
			user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
			// save user to the database
			userRepository.save(user);
		} catch (Exception e) {
			log.error("(UserServiceImpl) registerUser(): Failed to register user: " + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
		try {
			// find a user by username
			UserEntity userEntity = userRepository.findByUsername(loginRequestDTO.getUsername());
			// if a user does not exist or the password does not match, throw BusinessException
			if (ObjectUtils.isEmpty(userEntity)) {
				throw new BusinessException("Invalid username");
			}
			// check if the password matches
			if (!passwordEncoder.matches(loginRequestDTO.getPassword(), userEntity.getPassword())) {
				throw new BusinessException("Incorrect password.");
			}
			// set to UserInfoDTO for JWT token generation
			UserInfoDTO userInfoDTO = new UserInfoDTO();
			userInfoDTO.setId(userEntity.getId());
			userInfoDTO.setUsername(userEntity.getUsername());
			userInfoDTO.setEmail(userEntity.getEmail());
			userInfoDTO.setRole(userEntity.getRole());
			userInfoDTO.setIsVerified(userEntity.isVerified());
			userInfoDTO.setAccountLevel(userEntity.getAccountLevel().name());
			// generate JWT token
			String token = jwtUtil.generateToken(userInfoDTO);
			String refreshToken = jwtUtil.generateRefreshToken(userInfoDTO);
			// update last login time
			userEntity.setLastLogin(LocalDateTime.now());
			// save the updated user entity
			userRepository.save(userEntity);
			// create and return LoginResponseDTO
			LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
			loginResponseDTO.setId(userEntity.getId());
			loginResponseDTO.setUsername(userEntity.getUsername());
			loginResponseDTO.setBio(userEntity.getBio());
			loginResponseDTO.setProfilePicture(userEntity.getProfilePicture());
			loginResponseDTO.setCoverImage(userEntity.getCoverImage());
			loginResponseDTO.setEmail(userEntity.getEmail());
			loginResponseDTO.setAccessToken(token);
			loginResponseDTO.setRefreshToken(refreshToken);
			return loginResponseDTO;
		} catch (Exception e) {
			log.error("(UserServiceImpl) loginUser(): Failed to login user: " + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshToken) {
		try {
			// validate the refresh token
			if (!jwtUtil.validateToken(refreshToken.getRefreshToken())) {
				throw new BusinessException("Invalid refresh token.");
			}
			// extract user info from the refresh token
			UserInfoDTO userInfo = jwtUtil.extractUserInfo(refreshToken.getRefreshToken());
			// generate a new access token
			String newAccessToken = jwtUtil.generateToken(userInfo);
			String newRefreshToken = jwtUtil.generateRefreshToken(userInfo);
			// create and return RefreshTokenResponseDTO
			RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();
			response.setAccessToken(newAccessToken);
			response.setRefreshToken(newRefreshToken);
			return response;
		} catch (Exception e) {
			log.error("(UserServiceImpl) refreshToken(): Failed to refresh token: " + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}
}
