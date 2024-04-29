 package com.tsfn.sec.controller.request;

import java.util.List;
 
import com.tsfn.sec.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<UserRole> roles;
}
