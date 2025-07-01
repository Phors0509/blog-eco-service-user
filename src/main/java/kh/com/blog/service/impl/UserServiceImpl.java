package kh.com.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
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
			throw new BusinessException("(UserServiceImpl) registerUser(): Failed to register user:  " + e.getMessage());
		}
	}

	@Override
	public void loginUser(LoginRequestDTO loginRequestDTO) {
		try {
			// find a user by username
			UserEntity userEntity = userRepository.findByUsername(loginRequestDTO.getUsername());
			// if a user does not exist or password does not match, throw BusinessException
			if (ObjectUtils.isEmpty(userEntity)) {
				throw new BusinessException("Invalid credentials: User not found.");
			}
			// check if the password matches
			if (!passwordEncoder.matches(loginRequestDTO.getPassword(), userEntity.getPassword())) {
				throw new BusinessException("Invalid credentials: Incorrect password.");
			}
			// update last login time
			userEntity.setLastLogin(LocalDateTime.now());
			// save the updated user entity
			userRepository.save(userEntity);
		} catch (Exception e) {
			throw new BusinessException("(UserServiceImpl) loginUser(): Failed to login user: " + e.getMessage());
		}
	}
}
