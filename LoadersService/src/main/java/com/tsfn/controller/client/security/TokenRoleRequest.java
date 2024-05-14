package com.tsfn.controller.client.security;
 
import lombok.Data;

import java.util.List;

@Data
public class TokenRoleRequest {
    private String token;
    private List<Role> requiredRoles;
}