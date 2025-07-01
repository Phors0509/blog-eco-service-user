package kh.com.blog.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kh.com.blog.controller.vo.LoginRequestVO;
import kh.com.blog.controller.vo.RegisterRequestVO;
import kh.com.blog.dto.request.LoginRequestDTO;
import kh.com.blog.dto.request.RegisterRequestDTO;
import kh.com.blog.dto.response.ResponseMessageBuilder;
import kh.com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
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
	public ResponseMessageBuilder.ResponseMessage<Void> registerUser(@RequestBody RegisterRequestVO registerRequestVO) {
		this.userService.registerUser(objectMapper.convertValue(registerRequestVO, RegisterRequestDTO.class));
		return new ResponseMessageBuilder<Void>().success().build();
	}

	@PostMapping("/login")
	@Operation(method = "POST", summary = "User login", description = "Authenticate a user in the blog application.")
	public ResponseMessageBuilder.ResponseMessage<Void> loginUser(@RequestBody LoginRequestVO loginRequestVO) {
		this.userService.loginUser(objectMapper.convertValue(loginRequestVO, LoginRequestDTO.class));
		return new ResponseMessageBuilder<Void>().success().build();
	}

	@Operation(method = "GET", summary = "List all users", description = "Retrieve a list of all users in the blog application.")
	@GetMapping
	public String listUsers() {
		return "List of users will be implemented here.";
	}
}
