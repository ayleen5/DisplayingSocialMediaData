package com.tsfn.controller.client.security;


import lombok.Data;
 

@Data
public class VerifyTokenAndCheckRolesResponse {
   private boolean isVerifyTokenAndCheckRoles;
   private String message;
}
