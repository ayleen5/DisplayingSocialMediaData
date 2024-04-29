package com.tsfn.sec.service;

import com.tsfn.sec.controller.request.SignUpRequest;
import com.tsfn.sec.controller.request.SigninRequest;
import com.tsfn.sec.controller.request.UpdateRequest;
import com.tsfn.sec.controller.response.JwtAuthenticationResponse;
import com.tsfn.sec.controller.response.UpdateResponse;

public interface AuthenticationService {
	
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
    UpdateResponse update(UpdateRequest request);
   
}
