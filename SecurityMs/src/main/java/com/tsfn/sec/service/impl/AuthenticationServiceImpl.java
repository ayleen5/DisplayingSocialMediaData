package com.tsfn.sec.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tsfn.sec.controller.request.SignUpRequest;
import com.tsfn.sec.controller.request.SigninRequest;
import com.tsfn.sec.controller.request.UpdateRequest;
import com.tsfn.sec.controller.response.JwtAuthenticationResponse;
import com.tsfn.sec.controller.response.UpdateResponse;
import com.tsfn.sec.model.User;
import com.tsfn.sec.repository.UserRepository;
import com.tsfn.sec.service.AuthenticationService;
import com.tsfn.sec.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    
    
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
 
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
        		.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles())
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
 

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


	@Override
	public UpdateResponse update(UpdateRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).get();
		user.setFirstName(request.getFirstName());
	    user.setLastName(request.getLastName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    user.setRoles(request.getRoles());
	    userRepository.save(user);
	    return UpdateResponse.builder().build();
	}

 
}
