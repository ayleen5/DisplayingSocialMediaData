//package com.tsfn.controller.client.security;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//
//@Component
//public class SeccurityHelper {
//
//    @Autowired
//    private SecurityClient securityCleint;
//
//    public VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRoles(String authorizationHeader, List<Role> roles) {
//        String jwtToken = extractJwtToken(authorizationHeader);
//        TokenRoleRequest tokenRoleRequest = new TokenRoleRequest();
//        tokenRoleRequest.setRequiredRoles(roles);
//        tokenRoleRequest.setToken(jwtToken);
//        return securityCleint.verifyTokenAndCheckRoles(tokenRoleRequest);
//    }
//    
//	public CheackUserTokenResponse cheackUserToken(String authorizationHeader  ) {
//        String jwtToken = extractJwtToken(authorizationHeader);
//		TokenRequest tokenReuest = new TokenRequest();
// 		tokenReuest.setToken(jwtToken);
//        return securityCleint.cheackUserToken(tokenReuest);
//    }
//    
//    
//
//    private String extractJwtToken(String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }
//}
