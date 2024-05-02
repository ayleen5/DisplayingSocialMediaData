package com.tsfn.sec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.sec.controller.request.TokenRoleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsfn.sec.controller.request.SignUpRequest;
import com.tsfn.sec.controller.request.SigninRequest;
import com.tsfn.sec.controller.request.TokenRequest;
import com.tsfn.sec.controller.request.UpdateRequest;
import com.tsfn.sec.controller.response.CheackUserTokenResponse;
import com.tsfn.sec.controller.response.JwtAuthenticationResponse;
import com.tsfn.sec.controller.response.UpdateResponse;
import com.tsfn.sec.controller.response.VerifyTokenAndCheckRolesResponse;
import com.tsfn.sec.model.Role;
import com.tsfn.sec.service.AuthenticationService;
//import com.tsfn.sec.service.RoleService;
import com.tsfn.sec.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Value("${app.initial-user}")
	private String initialUserJson;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (!userServiceImpl.hasAdminUser()) {
				ObjectMapper objectMapper = new ObjectMapper();
				SignUpRequest createAdmin = objectMapper.readValue(initialUserJson, SignUpRequest.class);

				var createUser = authenticationService.signup(createAdmin);
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Only ADMIN users are allowed to add new users.");

			} else {
				if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
				}

				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				if (!userDetails.getAuthorities().stream()
						.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN)
							.body("Only ADMIN users are allowed to add new users.");
				}
			}
			// If the user is authenticated and is an admin, proceed with signup
			return ResponseEntity.ok(authenticationService.signup(request));
		} catch (Exception e) {
			String errorMessage = "An error occurred during signup: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
		return ResponseEntity.ok(authenticationService.signin(request));
	}

	@PostMapping("/update")
	public ResponseEntity<UpdateResponse> update(@RequestBody UpdateRequest request) {
		UpdateResponse updateResponse = new UpdateResponse();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
				updateResponse.setMessage("You are not authenticated.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(updateResponse);
			}

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			if (!userDetails.getAuthorities().stream()
					.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
				updateResponse.setMessage("Only ADMIN users are allowed to update  users.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(updateResponse);
			}

			return ResponseEntity.ok(authenticationService.update(request));
		} catch (Exception e) {
			String errorMessage = "An error occurred during signup: " + e.getMessage();
			updateResponse.setMessage(errorMessage);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateResponse);
		}
	}

	@PostMapping("/verifyTokenAndCheckRoles")
	public VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRoles(@RequestBody TokenRoleRequest tokenRoleRequest) {
		VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRolesResponse = new VerifyTokenAndCheckRolesResponse();
		try {
			verifyTokenAndCheckRolesResponse = authenticationService.verifyTokenAndCheckRoles(tokenRoleRequest);
			return verifyTokenAndCheckRolesResponse;
		} catch (Exception e) {
			verifyTokenAndCheckRolesResponse.setMessage(e.getMessage());
			return verifyTokenAndCheckRolesResponse;
		}
	}

	@PostMapping("/cheackUserToken")
	public CheackUserTokenResponse cheackUserToken(@RequestBody TokenRequest tokenRequest) {
		CheackUserTokenResponse cheackUserTokenResponse = new CheackUserTokenResponse();
		try {
			cheackUserTokenResponse = authenticationService.cheackUserToken(tokenRequest);
			return cheackUserTokenResponse;
		} catch (Exception e) {
			cheackUserTokenResponse.setMessage(e.getMessage());
			return cheackUserTokenResponse;
		}
	}
}
