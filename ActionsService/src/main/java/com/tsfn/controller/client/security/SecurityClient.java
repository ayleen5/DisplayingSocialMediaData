package com.tsfn.controller.client.security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "authentication-service", url = "http://localhost:8686")
public interface SecurityClient {
    
    @PostMapping("/verifyTokenAndCheckRoles")
    public VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRoles(@RequestBody TokenRoleRequest tokenRoleRequest);
    
}
