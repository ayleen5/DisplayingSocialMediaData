package com.tsfn.sec.controller.request;

import java.util.List;

import com.tsfn.sec.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRoleRequest {
    private String token;
    private List<Role> requiredRoles;
}