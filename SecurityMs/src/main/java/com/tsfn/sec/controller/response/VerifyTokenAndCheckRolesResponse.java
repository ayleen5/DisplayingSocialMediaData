package com.tsfn.sec.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyTokenAndCheckRolesResponse {
    private boolean isVerifyTokenAndCheckRoles;
    private String message;
}
