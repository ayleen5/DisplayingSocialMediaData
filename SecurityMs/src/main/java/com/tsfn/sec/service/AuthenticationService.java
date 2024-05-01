package com.tsfn.sec.service;

import java.util.List;

import com.tsfn.sec.controller.request.TokenRoleRequest;
import com.tsfn.sec.controller.request.SignUpRequest;
import com.tsfn.sec.controller.request.SigninRequest;
import com.tsfn.sec.controller.request.TokenRequest;
import com.tsfn.sec.controller.request.UpdateRequest;
import com.tsfn.sec.controller.response.CheackUserTokenResponse;
import com.tsfn.sec.controller.response.JwtAuthenticationResponse;
import com.tsfn.sec.controller.response.UpdateResponse;
import com.tsfn.sec.controller.response.VerifyTokenAndCheckRolesResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SigninRequest request);
    UpdateResponse update(UpdateRequest request);
    VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRoles(TokenRoleRequest tokenRoleRequest);
    
	  CheackUserTokenResponse cheackUserToken(TokenRequest tokenRequest);
}
