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
import com.tsfn.sec.controller.request.SignUpRequest;
import com.tsfn.sec.controller.request.SigninRequest;
import com.tsfn.sec.controller.request.UpdateRequest;
import com.tsfn.sec.controller.response.JwtAuthenticationResponse;
import com.tsfn.sec.controller.response.UpdateResponse;
import com.tsfn.sec.controller.response.VerifyTokenAndCheckRolesResponse;
import com.tsfn.sec.model.Role;
import com.tsfn.sec.service.AuthenticationService;
//import com.tsfn.sec.service.RoleService;
import com.tsfn.sec.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
	
	@Autowired
	private  AuthenticationService authenticationService;
	
	@Autowired
	private  UserServiceImpl userServiceImpl;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
	    try {
	        // Check if the user making the request is authenticated and is an admin
//	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	        
//	        
////	        if(!userServiceImpl.isAdmin())
////	        {
////	        	
////	        }
//	      //  else {
//	        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
//	        }
//
//	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//	        if (!userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
//	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN users are allowed to add new users.");
//	        }
	       // }
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
	    	UpdateResponse  updateResponse = new UpdateResponse(); 
    	try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     	 
	        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        	updateResponse.setMessage("You are not authenticated.");
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(updateResponse);
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        if (!userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
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
	 public VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRoles(@RequestBody TokenRoleRequest tokenRoleRequest){
    	VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRolesResponse = new VerifyTokenAndCheckRolesResponse();
    	try {
    		//verifyTokenAndCheckRolesResponse = authenticationService.verifyTokenAndCheckRoles(tokenRoleRequest);
    		verifyTokenAndCheckRolesResponse.setVerifyTokenAndCheckRoles(true);
	        return verifyTokenAndCheckRolesResponse;
	    } catch (Exception e) {
	    	verifyTokenAndCheckRolesResponse.setMessage(e.getMessage());
	    	return verifyTokenAndCheckRolesResponse;
	    }
    }
}
