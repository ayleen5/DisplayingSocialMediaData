package com.tsfn.sec.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleId implements Serializable {
    private User user;
    private RoleEntity role;

    // constructors, equals, hashCode (if necessary)...
}
