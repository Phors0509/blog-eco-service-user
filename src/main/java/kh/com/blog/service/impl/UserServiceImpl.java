package kh.com.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RefreshTokenRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.LoginResponseDTO;
import kh.com.blog.dto.response.RefreshTokenResponseDTO;
import kh.com.blog.dto.response.UserDetailResponseDTO;
import kh.com.blog.dto.response.UserInfoDTO;
import kh.com.blog.entity.UserEntity;
import kh.com.blog.exception.BusinessException;
import kh.com.blog.mapper.UserMapper;
import kh.com.blog.repository.UserRepository;
import kh.com.blog.security.JwtService;
import kh.com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final JwtService jwtService;
	private final UserMapper mapper;

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
			userInfoDTO.setAccountLevel(userEntity.getAccountLevel());
			// generate JWT token
			String token = jwtService.generateToken(userInfoDTO);
			String refreshToken = jwtService.generateRefreshToken(userInfoDTO);
			// update last login time
			userEntity.setLastLogin(LocalDateTime.now());
			// save the updated user entity
			userRepository.save(userEntity);
			// create and return LoginResponseDTO
			LoginResponseDTO loginResponseDTO = mapper.fromLogin(userEntity);
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
			if (!jwtService.validateToken(refreshToken.getRefreshToken())) {
				throw new BusinessException("Invalid refresh token.");
			}
			// extract user info from the refresh token
			UserInfoDTO userInfo = jwtService.extractUserInfo(refreshToken.getRefreshToken());
			// generate a new access token
			String newAccessToken = jwtService.generateToken(userInfo);
			String newRefreshToken = jwtService.generateRefreshToken(userInfo);
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

	@Override
	public UserDetailResponseDTO getCurrentUserDetail() {
		try {
			var authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getPrincipal().toString();
			if (ObjectUtils.isEmpty(username)) {
				throw new BusinessException("User is not authenticated.");
			}
			// find the user by username
			UserEntity userEntity = userRepository.findByUsername(username);
			// if the user does not exist, throw BusinessException
			if (ObjectUtils.isEmpty(userEntity)) {
				throw new BusinessException("User not found.");
			}
			// convert UserEntity to UserDetailResponseDTO
			return mapper.from(userEntity);
		} catch (Exception e) {
			log.error("(UserServiceImpl) getCurrentUserDetail(): Failed to get current user detail: " + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}
}
