package kh.com.blog.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kh.com.blog.controller.vo.request.LoginRequestVO;
import kh.com.blog.controller.vo.request.RefreshTokenRequestVO;
import kh.com.blog.controller.vo.request.RegisterRequestVO;
import kh.com.blog.controller.vo.response.LoginResponseVO;
import kh.com.blog.controller.vo.response.RefreshTokenResponseVO;
import kh.com.blog.controller.vo.response.UserDetailResponseVO;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RefreshTokenRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.ResponseMessageBuilder;
import kh.com.blog.dto.response.UserDetailResponseDTO;
import kh.com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "[API] Users", description = "APIs for managing users for the blog application.")
public class UserController {

	private final UserService userService;
	private final ObjectMapper objectMapper;

	@PostMapping("/register")
	@Operation(method = "POST", summary = "Register a new user", description = "Create a new user in the blog application.")
	public ResponseMessageBuilder.ResponseMessage<Void> registerUser(@RequestBody @Validated RegisterRequestVO registerRequestVO) {
		this.userService.registerUser(objectMapper.convertValue(registerRequestVO, RegisterRequestDTO.class));
		return new ResponseMessageBuilder<Void>().success().build();
	}

	@PostMapping("/login")
	@Operation(method = "POST", summary = "User login", description = "Authenticate a user in the blog application.")
	public ResponseMessageBuilder.ResponseMessage<LoginResponseVO> loginUser(@RequestBody @Validated LoginRequestVO loginRequestVO) {
		LoginResponseVO loginResponseVO =
				objectMapper.convertValue(this.userService.loginUser(
						objectMapper.convertValue(loginRequestVO, LoginRequestDTO.class)), LoginResponseVO.class);
		return new ResponseMessageBuilder<LoginResponseVO>().addData(loginResponseVO).success().build();
	}

	@PostMapping("/refresh-token")
	@Operation(method = "POST", summary = "Refresh user token", description = "Refresh the authentication token for a user in the blog application.")
	public ResponseMessageBuilder.ResponseMessage<RefreshTokenResponseVO> refreshToken(@RequestBody @Validated RefreshTokenRequestVO refreshToken) {
		RefreshTokenResponseVO refreshTokenResponseVO =
				objectMapper.convertValue(this.userService.refreshToken(
						objectMapper.convertValue(refreshToken, RefreshTokenRequestDTO.class)), RefreshTokenResponseVO.class);
		return new ResponseMessageBuilder<RefreshTokenResponseVO>().addData(refreshTokenResponseVO).success().build();
	}

	@GetMapping("/me")
	@Operation(method = "GET", summary = "Get current user info", description = "Retrieve the information of the currently authenticated user.")
	public ResponseMessageBuilder.ResponseMessage<UserDetailResponseVO> getCurrentUserDetail() {
		UserDetailResponseDTO userDetailResponseDTO = this.userService.getCurrentUserDetail();
		UserDetailResponseVO userDetailResponseVO = objectMapper.convertValue(userDetailResponseDTO, UserDetailResponseVO.class);
		return new ResponseMessageBuilder<UserDetailResponseVO>().addData(userDetailResponseVO).success().build();
	}

	@Operation(method = "GET", summary = "List all users", description = "Retrieve a list of all users in the blog application.")
	@GetMapping
	public String listUsers() {
		return "List of users will be implemented here.";
	}
}
